package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.factory.LocaleTextFactory;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by cghislai on 31/03/16.
 */
@RunWith(Arquillian.class)
public class StockEntriesTest extends JPATestBase {
    @EJB
    CompanyService companyService;
    @EJB
    SaleService saleService;
    @EJB
    CountryService countryService;
    @EJB
    StockService stockService;
    @EJB
    LocaleTextFactory localeTextFactory;
    @EJB
    RegistrationService registrationService;


    private Sale saleA;
    private Sale saleB;
    private ItemVariant variantA;
    private ItemVariant variantB;


    @Test
    public void testCreateStockEntry() {
        createItems();

        // Stock should be empty
        assertCurrentStockValue(variantA, defaultStock, null);
        assertCurrentStockValue(variantB, defaultStock, null);

        // Create saleA
        saleA = createSale();
        Assert.assertFalse(saleA.isClosed());

        // Add variant A
        ItemVariantSale variantASaleA = createItemVariantSale(saleA, variantA);
        // 2 units
        variantASaleA.setQuantity(BigDecimal.valueOf(2));
        variantASaleA = saleService.saveItemSale(variantASaleA);

        // Stock should still be empty
        assertCurrentStockValue(variantA, defaultStock, null);

        // Close sale
        saleA = saleService.closeSale(saleA);
        Assert.assertTrue(saleA.isClosed());

        // Stock should be set to -2
        assertCurrentStockValue(variantA, defaultStock, -2);

        // Reopen the sale
        saleA = saleService.reopenSale(saleA);
        Assert.assertFalse(saleA.isClosed());

        // Stock should be set back to null (no entries)
        assertCurrentStockValue(variantA, defaultStock, null);


        // Create Initial stock values of 10 units
        ItemStock initialVariantAStock = stockService.adaptStock(ZonedDateTime.now(), defaultStock, variantA, BigDecimal.valueOf(10), "init", StockChangeType.INITIAL, null);
        assertCurrentStockValue(variantA, defaultStock, 10);
        ItemStock initialVariantBStock = stockService.adaptStock(ZonedDateTime.now(), defaultStock, variantB, BigDecimal.valueOf(10), "init", StockChangeType.INITIAL, null);
        assertCurrentStockValue(variantB, defaultStock, 10);

        // Create Sale B
        saleB = createSale();
        // Add 5 items of each
        ItemVariantSale variantASaleB = createItemVariantSale(saleB, variantA);
        variantASaleB.setQuantity(BigDecimal.valueOf(5));
        variantASaleB = saleService.saveItemSale(variantASaleB);
        ItemVariantSale variantBSaleB = createItemVariantSale(saleB, variantB);
        variantBSaleB.setQuantity(BigDecimal.valueOf(5));
        variantBSaleB = saleService.saveItemSale(variantBSaleB);

        // Close it
        saleB = saleService.closeSale(saleB);
        // Stocks should contains 5 units each
        ItemStock afterSaleBVariantAStock = assertCurrentStockValue(variantA, defaultStock, 5);
        ItemStock afterSaleBVariantBStock = assertCurrentStockValue(variantB, defaultStock, 5);

        // Variant A stock item should follow initial stock item
        ItemStock beforeSaleBVariantAStock = afterSaleBVariantAStock.getPreviousItemStock();
        Assert.assertEquals(initialVariantAStock, beforeSaleBVariantAStock);

        // Close the first Sale
        saleA = saleService.closeSale(saleA);

        // Stock A should contains 3 units
        ItemStock afterSaleAVariantAStock = assertCurrentStockValue(variantA, defaultStock, 3);

        // Previous stock entry should be the one after sale B
        ItemStock beforeSaleAVariantAStock = afterSaleAVariantAStock.getPreviousItemStock();
        Assert.assertEquals(beforeSaleAVariantAStock, afterSaleBVariantAStock);

        // Reopen the second sale
        saleB = saleService.reopenSale(saleB);
        // Stock should be back to 3 + 5 = 8
        ItemStock currentVariantAStock = assertCurrentStockValue(variantA, defaultStock, 8);
        // Parent stock entry should be the initial stock
        ItemStock previousVariantAStock = currentVariantAStock.getPreviousItemStock();
        Assert.assertEquals(previousVariantAStock, initialVariantAStock);

        // Reopen the first sale
        saleA = saleService.reopenSale(saleA);
        // Stock should be back at 10
        assertCurrentStockValue(variantA, defaultStock, 10);

    }

    public ItemStock assertCurrentStockValue(ItemVariant itemVariant, Stock stock, Integer expectedValue) {
        ItemStock variantStock = stockService.findItemStock(itemVariant, stock, ZonedDateTime.now());
        if (expectedValue == null) {
            Assert.assertNull(variantStock);
            return null;
        }
        Assert.assertNotNull(variantStock);
        BigDecimal quantity = variantStock.getQuantity();
        BigDecimal expectedQuantity = BigDecimal.valueOf(expectedValue);
        int comparisonValue = quantity.compareTo(expectedQuantity);
        Assert.assertEquals(comparisonValue, 0);
        return variantStock;
    }

    public void createItems() {
        Item itemA = createItem("A", "A", BigDecimal.valueOf(9.99));
        variantA = getDefaultVariant(itemA);

        Item itemB = createItem("B", "B", BigDecimal.valueOf(8.99));
        itemB.setMultipleSale(true);
        itemB = stockService.saveItem(itemB);
        variantB = getDefaultVariant(itemB);
    }

}
