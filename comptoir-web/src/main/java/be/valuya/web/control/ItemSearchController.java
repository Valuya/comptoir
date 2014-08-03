package be.valuya.web.control;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class ItemSearchController implements Serializable {

    @EJB
    private transient StockService stockService;
    @Inject
    private transient LoginController loginController;
    //
    private ItemSearch itemSearch;

    @PostConstruct
    public void init() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();
        
        itemSearch = new ItemSearch();
        itemSearch.setCompany(company);
    }

    public List<Item> completeName(String reference) {
        Pagination<Item, ItemColumn> pagination = new Pagination<>();
        pagination.setMaxResults(10);

        List<Item> items = stockService.findItems(itemSearch, pagination);
        return items;
    }

    public ItemSearch getItemSearch() {
        return itemSearch;
    }

}
