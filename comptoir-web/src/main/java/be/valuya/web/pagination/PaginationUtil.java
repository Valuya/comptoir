package be.valuya.web.pagination;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Sorting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class PaginationUtil {

    public static <T, C extends Column<T>> List<Sorting<C>> createSortings(List<SortMeta> multiSortMeta, Function<String, C> conversionFunction) {
        List<Sorting<C>> sortings = new ArrayList<>();
        multiSortMeta.stream().
                filter(s -> s.getSortOrder() != SortOrder.UNSORTED).
                map(
                        sortMeta -> {
                            String sortField = sortMeta.getSortField();
                            C sortColumn = conversionFunction.apply(sortField);
                            SortOrder sortOrder = sortMeta.getSortOrder();
                            Sorting<C> sorting = new Sorting<>(sortColumn, sortOrder == SortOrder.ASCENDING);
                            return sorting;
                        }).
                collect(Collectors.toList());
        return sortings;
    }
}
