package be.valuya.comptoir.web.control;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sorting;
import be.valuya.comptoir.web.pagination.PaginationUtil;
import be.valuya.comptoir.web.view.Views;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class ItemListController implements Serializable {

    @EJB
    private transient StockService stockService;
    @Inject
    private transient LoginController loginController;
    private final ItemLazyDataModel itemLazyDataModel = new ItemLazyDataModel();
    //
    private ItemSearch itemSearch;

    public String actionList() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();

        itemSearch = new ItemSearch();
        itemSearch.setCompany(company);
        
        itemLazyDataModel.setRowCount(10);

        return Views.ITEM_LIST;
    }

    public void actionSearch() {
    }

    public ItemLazyDataModel getItemLazyDataModel() {
        return itemLazyDataModel;
    }

    public ItemSearch getItemSearch() {
        return itemSearch;
    }

    private class ItemLazyDataModel extends LazyDataModel<Item> {

        @Override
        public List<Item> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
            Pagination<Item, ItemColumn> pagination = new Pagination<>();
            pagination.setOffset(first);
            pagination.setMaxResults(pageSize);

            List<Sorting<ItemColumn>> sortings = PaginationUtil.createSortings(multiSortMeta, ItemColumn::valueOf);
            pagination.setSortings(sortings);

            List<Item> items = stockService.findItems(itemSearch, pagination);
            
            setRowCount(items.size());
            
            return items;
        }
    }

}
