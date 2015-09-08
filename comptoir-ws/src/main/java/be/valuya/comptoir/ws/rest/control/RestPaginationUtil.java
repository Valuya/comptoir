package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import be.valuya.comptoir.ws.config.HeadersConfig;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class RestPaginationUtil {

    private static final String SORT_PARAMETER = "sort";
    private static final String LENGTH_PARAMETR = "length";
    private static final String OFFSET_PARAMETER = "offset";

    public <T, C extends Column<T>> Pagination<T, C> extractPagination(UriInfo uriInfo, Function<String, C> conversionFunction) {
        MultivaluedMap<String, String> pathParameters = uriInfo.getQueryParameters();

        int offset = 0;
        if (pathParameters.containsKey(OFFSET_PARAMETER)) {
            List<String> offsetValues = pathParameters.get(OFFSET_PARAMETER);
            offset = offsetValues.stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        }

        int length = 0;
        if (pathParameters.containsKey(LENGTH_PARAMETR)) {
            List<String> lengthValues = pathParameters.get(LENGTH_PARAMETR);
            length = lengthValues.stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        }
        List<Sort<C>> sorts = null;
        if (pathParameters.containsKey(SORT_PARAMETER)) {
            List<String> sortValues = pathParameters.get(SORT_PARAMETER);
            sorts = sortValues.stream()
                    .map(sortDef -> parseSort(sortDef, conversionFunction))
                    .collect(Collectors.toList());
        }

        return new Pagination<>(offset, length, sorts);
    }

    private <T, C extends Column<T>> Sort<C> parseSort(String sortDef, Function<String, C> conversionFunction) {
        String[] sortDefs = sortDef.split("-");
        if (sortDefs.length != 2) {
            return null;
        }
        return new Sort<>(conversionFunction.apply(sortDefs[0]), sortDefs[1].equals("asc"));
    }

    public <T, C extends Column<T>> void addResultCount(HttpServletResponse response, Pagination<T, C> pagination) {
        Long allResultCount = pagination.getAllResultCount();
        if (allResultCount == null) {
            return;
        }
        response.setHeader(HeadersConfig.LIST_RESULTS_COUNT_HEADER, Long.toString(allResultCount));
    }
}
