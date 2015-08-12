package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class RestPaginationUtil {

    /**
     * Extract the pagination from the search path segment parameters.
     * <pre>
     * .../resouce/search;offset=offsetValue;maxResults=maxResultsValue;sort=sortValues
     * </pre>
     * Where offsetValue and maxResultsValue are integer.
     * SortValue is a comma-separated list of sorts:
     * <pre>
     *  sort=col1-asc,col2-desc,col3-asc
     * </pre>
     * where col* is the name of the column.
     * 
     * @param <T>
     * @param <C>
     * @param uriInfo
     * @param conversionFunction
     * @return 
     */
    public <T, C extends Column<T>> Pagination<T, C> extractPagination(UriInfo uriInfo, Function<String, C> conversionFunction) {
        MultivaluedMap<String, String> matrixParameters = uriInfo.getPathSegments()
                .stream()
                .filter((PathSegment t) -> t.getPath().equals("search"))
                .findFirst()
                .map((pathsegment) -> pathsegment.getMatrixParameters())
                .orElse(null);
        if (matrixParameters == null) {
            return null;
        }

        int offset = 0;
        int maxResults = 0;
        List<Sort<C>> sorts = null;

        final List<String> offsetParameter = matrixParameters.get("offset");
        if (offsetParameter != null) {
            offset = offsetParameter.stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        }
        List<String> maxResultsParameter = matrixParameters.get("maxResults");
        if (maxResultsParameter != null) {
            maxResults = maxResultsParameter.stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        }
        final List<String> sortsparameter = matrixParameters.get("sort");
        if (sortsparameter != null) {
            sorts = sortsparameter.stream()
                    .map(sortDef -> parseSort(sortDef, conversionFunction))
                    .collect(Collectors.toList());
        }
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
