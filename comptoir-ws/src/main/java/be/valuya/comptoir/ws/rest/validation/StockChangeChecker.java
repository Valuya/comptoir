package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.StockChangeType;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class StockChangeChecker {

    public void checkStockChangeType(ItemStock itemStock, StockChangeType... expectedTypes) {
        StockChangeType stockChangeType = itemStock.getStockChangeType();
        if (stockChangeType == null) {
            throw new AssertionError("Stock change type expected");
        }
        List<StockChangeType> stockChangeTypes = Arrays.asList(expectedTypes);
        if (!stockChangeTypes.contains(stockChangeType)) {
            throw new AssertionError("Stock change type is not allowed : "+stockChangeType);
        }
    }

    public void checkPreviousStockExists(ItemStock itemStock, boolean expectedExists) {
        ItemStock previousItemStock = itemStock.getPreviousItemStock();
        boolean previousExists = previousItemStock != null;
        if (previousExists != expectedExists) {
            throw new AssertionError("Previous stock " + (expectedExists ? "": "not ") + "expected");
        }
    }

}
