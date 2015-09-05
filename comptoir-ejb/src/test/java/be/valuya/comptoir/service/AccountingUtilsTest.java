package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class AccountingUtilsTest {

    @Test
    public void testCalcSale() {
        Price price1 = new Price();
        price1.setVatExclusive(BigDecimal.valueOf(1000, 2));
        price1.setVatRate(BigDecimal.valueOf(21, 2));
        price1.setDiscountRatio(BigDecimal.valueOf(20, 2));

        Price price2 = new Price();
        price2.setVatExclusive(BigDecimal.valueOf(10000, 2));
        price2.setVatRate(BigDecimal.valueOf(21, 2));
        price2.setDiscountRatio(BigDecimal.valueOf(0, 2));

        Item item1 = new Item();
        item1.setCurrentPrice(price1);

        ItemVariant itemVariant1 = new ItemVariant();
        itemVariant1.setItem(item1);

        Item item2 = new Item();
        item2.setCurrentPrice(price2);

        ItemVariant itemVariant2 = new ItemVariant();
        itemVariant2.setItem(item2);

        ItemVariantSale itemSale1 = new ItemVariantSale();
        itemSale1.setItemVariant(itemVariant1);
        itemSale1.setPrice(price1);
        itemSale1.setQuantity(BigDecimal.valueOf(2, 0));

        ItemVariantSale itemSale2 = new ItemVariantSale();
        itemSale2.setItemVariant(itemVariant2);
        itemSale2.setPrice(price2);
        itemSale2.setQuantity(BigDecimal.valueOf(1, 0));

        Sale sale = new Sale();

        List<ItemVariantSale> itemSales = Arrays.asList(itemSale1, itemSale2);

        Sale adjustedSale = AccountingUtils.calcSale(sale, itemSales);
        BigDecimal vatExclusiveAmount = adjustedSale.getVatExclusiveAmount();
        BigDecimal vatAmount = adjustedSale.getVatAmount();

        String baseStr = MessageFormat.format("Base: {0}", vatExclusiveAmount);
        System.out.println(baseStr);

        String taxesStr = MessageFormat.format("Taxes: {0}", vatAmount);
        System.out.println(taxesStr);

        Assert.assertTrue(vatExclusiveAmount.compareTo(BigDecimal.valueOf(11600, 2)) == 0);
        Assert.assertTrue(vatAmount.compareTo(BigDecimal.valueOf(2436, 2)) == 0);
    }

}
