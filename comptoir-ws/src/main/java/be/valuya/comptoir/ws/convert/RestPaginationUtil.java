package be.valuya.comptoir.ws.convert;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import be.valuya.comptoir.ws.api.parameters.ApiParameters;
import be.valuya.comptoir.ws.api.parameters.PaginationParams;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class RestPaginationUtil {


    public <T, C extends Column<T>> Pagination<T, C> parsePagination(PaginationParams paginationParams, Function<String, C> conversionFunction) {
        int offset = paginationParams.getOffsetOptional()
                .orElse(0);
        int length = paginationParams.getLengthOptional()
                .orElse(0);
        List<Sort<C>> sorts = paginationParams.getSortsOptional()
                .map(sort -> this.parseSort(sort, conversionFunction))
                .map(List::of)
                .orElseGet(ArrayList::new);

        return new Pagination<>(offset, length, sorts);
    }


    /**
     * Deprecated: use parsePagination and inject the PaginationParam in method parameters
     *
     * @param uriInfo
     * @param conversionFunction
     * @param <T>
     * @param <C>
     * @return
     */
    @Deprecated
    public <T, C extends Column<T>> Pagination<T, C> extractPagination(UriInfo uriInfo, Function<String, C> conversionFunction) {
        MultivaluedMap<String, String> pathParameters = uriInfo.getQueryParameters();

        int offset = 0;
        if (pathParameters.containsKey(ApiParameters.PAGINATION_OFFSET_QUERY_PARAM)) {
            List<String> offsetValues = pathParameters.get(ApiParameters.PAGINATION_OFFSET_QUERY_PARAM);
            offset = offsetValues.stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        }

        int length = 0;
        if (pathParameters.containsKey(ApiParameters.PAGINATION_LENGTH_QUERY_PARAM)) {
            List<String> lengthValues = pathParameters.get(ApiParameters.PAGINATION_LENGTH_QUERY_PARAM);
            length = lengthValues.stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        }
        List<Sort<C>> sorts = null;
        if (pathParameters.containsKey(ApiParameters.PAGINATION_SORT_QUERY_PARAM)) {
            List<String> sortValues = pathParameters.get(ApiParameters.PAGINATION_SORT_QUERY_PARAM);
            sorts = sortValues.stream()
                    .map(sortDef -> parseSort(sortDef, conversionFunction))
                    .filter(sort -> sort != null)
                    .collect(Collectors.toList());
        }

        return new Pagination<>(offset, length, sorts);
    }

    private <T, C extends Column<T>> Sort<C> parseSort(String sortDef, Function<String, C> conversionFunction) {
        String[] sortDefs = sortDef.split(":");
        if (sortDefs.length != 2) {
            return null;
        }
        C column;
        try {
            column = conversionFunction.apply(sortDefs[0]);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid sort column: " + sortDefs[0], e);
        }
        return new Sort<>(column, sortDefs[1].equalsIgnoreCase("asc"));
    }

    public <T, C extends Column<T>> void addResultCount(HttpServletResponse response, Pagination<T, C> pagination) {
        Long allResultCount = pagination.getAllResultCount();
        if (allResultCount == null) {
            return;
        }
        response.setHeader(ApiParameters.LIST_RESULTS_COUNT_HEADER, Long.toString(allResultCount));
    }
}
