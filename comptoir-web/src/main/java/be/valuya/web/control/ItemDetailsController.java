package be.valuya.web.control;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.StockService;
import be.valuya.web.view.Views;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
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
public class ItemDetailsController implements Serializable {

    @EJB
    private transient StockService stockService;
    @Inject
    private LoginController loginController;
    @Inject
    private ItemListController itemListController;
    //
    private Item item;
    private List<ItemStock> itemStocks;
    private Stock selectedStock;
    private BigDecimal newQuantity;
    private String comment;

    public String actionNew() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();

        item = new Item();
        item.setCompany(company);

        newQuantity = BigDecimal.ONE;
        
        return Views.ITEM_DETAILS;
    }

    public String actionDetails(Item item) {
        this.item = item;
        searchItemStocks();
        
        return Views.ITEM_DETAILS;
    }

    public void actionSave() {
        item = stockService.saveItem(item, selectedStock, newQuantity);
    }

    public String actionCancel() {
        item = null;
        return itemListController.actionList();
    }

    public void actionSelectStockFromItemStock(ItemStock itemStock) {
        selectedStock = itemStock.getStock();
        newQuantity = itemStock.getQuantity();
    }

    public void handleStockSelection(Stock stock) {
        selectedStock = stock;
        newQuantity = null;
    }

    public void actionAdaptStock() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        stockService.adaptStock(dateTime, selectedStock, item, newQuantity, StockChangeType.ADJUSTMENT, comment);
    }

    //<editor-fold defaultstate="collapsed" desc="get/set...">
    public Item getItem() {
        return item;
    }

    public List<ItemStock> getItemStocks() {
        return itemStocks;
    }

    public Stock getSelectedStock() {
        return selectedStock;
    }

    public void setSelectedStock(Stock selectedStock) {
        this.selectedStock = selectedStock;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //</editor-fold>
    private void searchItemStocks() {
        itemStocks = stockService.findItemStocks(item);
    }

}
