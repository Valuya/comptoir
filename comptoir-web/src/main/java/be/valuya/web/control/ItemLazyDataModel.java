package be.valuya.web.control;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sorting;
import be.valuya.web.pagination.PaginationUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@SessionScoped
public class ItemLazyDataModel extends LazyDataModel<Item> implements Serializable {

    @EJB
    private StockService stockService;
    @Inject
    private ItemListController itemController;

    @Override
    public List<Item> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        ItemSearch itemSearch = itemController.getItemSearch();

        Pagination<Item, ItemColumn> pagination = new Pagination<>();
        pagination.setOffset(first);
        pagination.setMaxResults(pageSize);

        List<Sorting<ItemColumn>> sortings = PaginationUtil.createSortings(multiSortMeta, ItemColumn::valueOf);
        pagination.setSortings(sortings);

        List<Item> items = stockService.findItems(itemSearch, pagination);
        return items;
    }

}
