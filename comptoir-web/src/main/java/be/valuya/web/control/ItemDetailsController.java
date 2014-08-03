package be.valuya.web.control;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.service.StockService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
    private Item item;
    private List<ItemStock> itemStocks;
    private Stock selectedStock;
    private BigDecimal newQuantity;
    private String comment;

    public void actionShowItem(Item item) {
        this.item = item;
        searchItemStocks();
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
