package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSalePriceDetails;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import be.valuya.comptoir.model.commercial.SalePriceDetails;
import be.valuya.comptoir.model.thirdparty.Customer;
import org.junit.Assert;
import org.junit.Test;

/**
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


        Sale sale = new Sale();
        Customer customer = new Customer();
        sale.setCustomer(customer);

        ItemVariantSale itemSale1 = new ItemVariantSale();
        itemSale1.setItemVariant(itemVariant1);
        itemSale1.setPrice(price1);
        itemSale1.setQuantity(BigDecimal.valueOf(2, 0));
        itemSale1.setSale(sale);

        ItemVariantSale itemSale2 = new ItemVariantSale();
        itemSale2.setItemVariant(itemVariant2);
        itemSale2.setPrice(price2);
        itemSale2.setQuantity(BigDecimal.valueOf(1, 0));
        itemSale2.setSale(sale);

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


    @Test
    public void testCalcSale2() {
        // 10% discount on all items, non cumulable
        Customer customer = new Customer();
        customer.setDiscountRate(BigDecimal.valueOf(10, 2));
        customer.setDiscountCumulable(false);

        Sale sale = new Sale();
        sale.setCustomer(customer);
        // additional 50% on everything
        sale.setDiscountRatio(BigDecimal.valueOf(50, 2));

        // 1st item; 10€ htva, 0% discount, 21% vat, 2 units
        Price price1 = new Price();
        price1.setVatExclusive(BigDecimal.valueOf(1000, 2));
        price1.setVatRate(BigDecimal.valueOf(21, 2));
        Item item1 = new Item();
        item1.setCurrentPrice(price1);
        ItemVariant itemVariant1 = new ItemVariant();
        itemVariant1.setItem(item1);
        ItemVariantSale itemSale1 = new ItemVariantSale();
        itemSale1.setItemVariant(itemVariant1);
        itemSale1.setPrice(price1);
        itemSale1.setQuantity(BigDecimal.valueOf(2, 0));
        itemSale1.setSale(sale);
        itemSale1.setIncludeCustomerDiscount(true);

        // 2nd item: 12€ htva, 25% discount; 0% vat, 1 unit
        Price price2 = new Price();
        price2.setVatExclusive(BigDecimal.valueOf(1200, 2));
        price2.setVatRate(BigDecimal.valueOf(0, 2));
        price2.setDiscountRatio(BigDecimal.valueOf(25, 2));
        Item item2 = new Item();
        item2.setCurrentPrice(price2);
        ItemVariant itemVariant2 = new ItemVariant();
        itemVariant2.setItem(item2);
        ItemVariantSale itemSale2 = new ItemVariantSale();
        itemSale2.setItemVariant(itemVariant2);
        itemSale2.setPrice(price2);
        itemSale2.setQuantity(BigDecimal.valueOf(1, 0));
        itemSale2.setSale(sale);
        itemSale2.setIncludeCustomerDiscount(true);


        ItemVariantSalePriceDetails priceDetails1 = AccountingUtils.calcItemVariantSalePriceDetails(itemSale1);
        assertAmountEqual(1000, priceDetails1.getUnitPriceVatExclusive());
        assertAmountEqual(2000, priceDetails1.getTotalVatExclusivePriorDiscount());
        assertAmountEqual(10, priceDetails1.getEffectiveDiscountRatio());
        assertAmountEqual(1800, priceDetails1.getTotalVatExclusive());
        // 18€ * 1.21 = 21.78
        assertAmountEqual(2178, priceDetails1.getTotalVatInclusive());
        assertAmountEqual(378, priceDetails1.getVatAmount());


        ItemVariantSalePriceDetails priceDetails2 = AccountingUtils.calcItemVariantSalePriceDetails(itemSale2);
        assertAmountEqual(1200, priceDetails2.getUnitPriceVatExclusive());
        assertAmountEqual(1200, priceDetails2.getTotalVatExclusivePriorDiscount());
        assertAmountEqual(25, priceDetails2.getEffectiveDiscountRatio());
        assertAmountEqual(900, priceDetails2.getTotalVatExclusive());
        assertAmountEqual(900, priceDetails2.getTotalVatInclusive());
        assertAmountEqual(0, priceDetails2.getVatAmount());


        List<ItemVariantSale> itemSales = Arrays.asList(itemSale1, itemSale2);
        SalePriceDetails salePriceDetails = AccountingUtils.calcSalePriceDetails(sale, itemSales);
        assertAmountEqual(2700, salePriceDetails.getTotalPriceVatExclusivePriorSaleDiscount());
        assertAmountEqual(1350, salePriceDetails.getSaleDiscountAmount());
        assertAmountEqual(1350, salePriceDetails.getTotalPriceVatExclusive());
        // 3.78€ vat, with 50% discount => 1.89€ vat
        assertAmountEqual(189, salePriceDetails.getVatAmount());
        assertAmountEqual(1539, salePriceDetails.getTotalPriceVatInclusive());
    }

    @Test
    public void testSetSaleTotal() {
        // 10% discount on all items, non cumulable
        Customer customer = new Customer();
        customer.setDiscountRate(BigDecimal.valueOf(10, 2));
        customer.setDiscountCumulable(false);

        Sale sale = new Sale();
        sale.setCustomer(customer);

        // 1st item; 10€ htva, 0% discount, 21% vat, 2 units
        Price price1 = new Price();
        price1.setVatExclusive(BigDecimal.valueOf(1000, 2));
        price1.setVatRate(BigDecimal.valueOf(21, 2));
        Item item1 = new Item();
        item1.setCurrentPrice(price1);
        ItemVariant itemVariant1 = new ItemVariant();
        itemVariant1.setItem(item1);
        ItemVariantSale itemSale1 = new ItemVariantSale();
        itemSale1.setItemVariant(itemVariant1);
        itemSale1.setPrice(price1);
        itemSale1.setQuantity(BigDecimal.valueOf(2, 0));
        itemSale1.setSale(sale);
        itemSale1.setIncludeCustomerDiscount(true);

        // 2nd item: 12€ htva, 25% discount; 0% vat, 1 unit
        Price price2 = new Price();
        price2.setVatExclusive(BigDecimal.valueOf(1200, 2));
        price2.setVatRate(BigDecimal.valueOf(0, 2));
        price2.setDiscountRatio(BigDecimal.valueOf(25, 2));
        Item item2 = new Item();
        item2.setCurrentPrice(price2);
        ItemVariant itemVariant2 = new ItemVariant();
        itemVariant2.setItem(item2);
        ItemVariantSale itemSale2 = new ItemVariantSale();
        itemSale2.setItemVariant(itemVariant2);
        itemSale2.setPrice(price2);
        itemSale2.setQuantity(BigDecimal.valueOf(1, 0));
        itemSale2.setSale(sale);
        itemSale2.setIncludeCustomerDiscount(true);

        List<ItemVariantSale> itemSales = Arrays.asList(itemSale1, itemSale2);
        SalePriceDetails salePriceDetails = AccountingUtils.calcSalePriceDetails(sale, itemSales);
        assertAmountEqual(2700, salePriceDetails.getTotalPriceVatExclusivePriorSaleDiscount());
        assertAmountEqual(0, salePriceDetails.getSaleDiscountAmount());
        assertAmountEqual(2700, salePriceDetails.getTotalPriceVatExclusive());
        assertAmountEqual(378, salePriceDetails.getVatAmount());
        assertAmountEqual(3078, salePriceDetails.getTotalPriceVatInclusive());

        BigDecimal discountRate = AccountingUtils.calcDiscountRateFromTotalVatInclusive(sale, itemSales, BigDecimal.valueOf(1539, 2));
        assertAmountEqual(50, discountRate);
        sale.setDiscountRatio(discountRate);

        salePriceDetails = AccountingUtils.calcSalePriceDetails(sale, itemSales);
        assertAmountEqual(2700, salePriceDetails.getTotalPriceVatExclusivePriorSaleDiscount());
        assertAmountEqual(1350, salePriceDetails.getSaleDiscountAmount());
        assertAmountEqual(1350, salePriceDetails.getTotalPriceVatExclusive());
        assertAmountEqual(189, salePriceDetails.getVatAmount());
        assertAmountEqual(1539, salePriceDetails.getTotalPriceVatInclusive());
    }


    @Test
    public void testSetItemTotalVatInclusive() {
        // 10% discount on all items, non cumulable
        Customer customer = new Customer();
        customer.setDiscountRate(BigDecimal.valueOf(10, 2));
        customer.setDiscountCumulable(false);

        Sale sale = new Sale();
        sale.setCustomer(customer);

        // 2nd item: 12€ htva, 25% discount; 10% vat, 1 unit
        Price price2 = new Price();
        price2.setVatExclusive(BigDecimal.valueOf(1200, 2));
        price2.setVatRate(BigDecimal.valueOf(10, 2));
        price2.setDiscountRatio(BigDecimal.valueOf(25, 2));
        Item item2 = new Item();
        item2.setCurrentPrice(price2);
        ItemVariant itemVariant2 = new ItemVariant();
        itemVariant2.setItem(item2);
        ItemVariantSale itemSale2 = new ItemVariantSale();
        itemSale2.setItemVariant(itemVariant2);
        itemSale2.setPrice(price2);
        itemSale2.setQuantity(BigDecimal.valueOf(1, 0));
        itemSale2.setSale(sale);
        itemSale2.setIncludeCustomerDiscount(true);

        ItemVariantSalePriceDetails priceDetails2 = AccountingUtils.calcItemVariantSalePriceDetails(itemSale2);
        assertAmountEqual(1200, priceDetails2.getUnitPriceVatExclusive());
        assertAmountEqual(1200, priceDetails2.getTotalVatExclusivePriorDiscount());
        assertAmountEqual(25, priceDetails2.getEffectiveDiscountRatio());
        assertAmountEqual(900, priceDetails2.getTotalVatExclusive());
        assertAmountEqual(990, priceDetails2.getTotalVatInclusive());
        assertAmountEqual(90, priceDetails2.getVatAmount());

        // Set total to 10
        BigDecimal unitPrice = AccountingUtils.calcUnitPriceVatExclusiveFromTotalVatInclusive(itemSale2, BigDecimal.valueOf(1000, 2));
        price2.setVatExclusive(unitPrice);

        priceDetails2 = AccountingUtils.calcItemVariantSalePriceDetails(itemSale2);
        assertAmountEqual(1000, priceDetails2.getTotalVatInclusive());
    }

    private void assertAmountEqual(long expectedScale2, BigDecimal actual) {
        Assert.assertEquals(
                BigDecimal.valueOf(expectedScale2, 2),
                actual.setScale(2, RoundingMode.HALF_UP)
        );
    }
}
