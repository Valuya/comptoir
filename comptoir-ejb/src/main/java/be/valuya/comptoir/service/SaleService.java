package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import java.time.ZonedDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class SaleService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private StockService stockService;

    public Sale createSale(Stock stock, Sale sale, List<ItemSale> itemSales) {
        Sale managedSale = entityManager.merge(sale);

        for (ItemSale itemSale : itemSales) {
            ItemSale managedItemSale = entityManager.merge(itemSale);
            managedItemSale.setSale(managedSale);
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            stockService.adaptStockFromItemSale(zonedDateTime, stock, managedItemSale, StockChangeType.SALE, null);
        }

        // TODO: create accounting entry
        return managedSale;
    }

}
