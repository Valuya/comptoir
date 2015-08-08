package be.valuya.comptoir.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class RestPaginationUtil {

    public <T, C extends Column<T>> Pagination<T, C> extractPagination(UriInfo uriInfo, Function<String, C> conversionFunction) {
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();
        int offset = pathParameters.get("offset").stream()
                .findFirst()
                .map(Integer::parseInt)
                .orElse(0);
        int maxResults = pathParameters.get("offset").stream()
                .findFirst()
                .map(Integer::parseInt)
                .orElse(0);

        List<Sort<C>> sorts = pathParameters.get("sort").stream()
                .map(sortDef -> parseSort(sortDef, conversionFunction))
                .collect(Collectors.toList());

        return new Pagination<>(offset, maxResults, sorts);
    }

    private <T, C extends Column<T>> Sort<C> parseSort(String sortDef, Function<String, C> conversionFunction) {
        String[] sortDefs = sortDef.split("-");
        if (sortDefs.length != 2) {
            return null;
        }
        return new Sort<>(conversionFunction.apply(sortDefs[1]), sortDefs[2].equals("asc"));
    }

}
