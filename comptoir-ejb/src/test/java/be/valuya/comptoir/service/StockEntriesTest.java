package be.valuya.comptoir.service;

import be.valuya.comptoir.ComptoirEJB;
import be.valuya.comptoir.model.commercial.*;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.model.factory.LocaleTextFactory;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemVariantSearch;
import be.valuya.comptoir.model.search.StockSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.thirdparty.Employee;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Created by cghislai on 31/03/16.
 */
@RunWith(Arquillian.class)
public class StockEntriesTest {
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, ComptoirEJB.class.getPackage())
                .deleteClass(DbInitProducer.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return javaArchive;
    }

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


    private Company testCompany;
    private Stock testDefaultStock;
    private ItemVariant testVariantADefault;
    private ItemVariant testVariantAA;
    // MultipleSale
    private ItemVariant testVariantBDefault;

    @Test
    public void testCreateStockEntry() {
        Assert.assertNotNull(companyService);
        Long countCompanies = companyService.countCompanies();
        Assert.assertEquals("No companies yet", (long) countCompanies, 0L);

        // Create company
        createTestData();
        countCompanies = companyService.countCompanies();
        Assert.assertEquals("Company created", (long) countCompanies, 1L);

        // Stock should be empty
        assertCurrentStockValue(testVariantAA, testDefaultStock, null);

        // Create a sale
        Sale testSale = createTestSale();
        Assert.assertFalse(testSale.isClosed());

        // Create an itemSale
        ItemVariantSale variantSaleAA = createItemVariantSale(testSale, testVariantAA);
        variantSaleAA.setQuantity(BigDecimal.valueOf(2));
        variantSaleAA = saleService.saveItemSale(variantSaleAA);

        // Stock should still be empty
        assertCurrentStockValue(testVariantAA, testDefaultStock, null);

        // Close sale
        testSale = saleService.closeSale(testSale);
        Assert.assertTrue(testSale.isClosed());
        // Stock should be set to -2
        assertCurrentStockValue(testVariantAA, testDefaultStock, -2);

        // Reopen the sale
        testSale = saleService.reopenSale(testSale);
        Assert.assertFalse(testSale.isClosed());

        // Stock should be set back to null (no entries)
        assertCurrentStockValue(testVariantAA, testDefaultStock, null);

        // Create an initial stock entry for 10 units
        ItemStock initialItemStock = stockService.adaptStock(ZonedDateTime.now(), testDefaultStock, testVariantAA, BigDecimal.valueOf(10), "init", StockChangeType.INITIAL, null);
        assertCurrentStockValue(testVariantAA, testDefaultStock, 10);

        // Create a second sale
        Sale testSale2 = createTestSale();
        // Add a single variant
        ItemVariantSale variantSaleAA2 = createItemVariantSale(testSale2, testVariantAA);
        variantSaleAA2 = saleService.saveItemSale(variantSaleAA2);
        // Close it
        testSale2 = saleService.closeSale(testSale2);
        // Stock should contains 9 units
        ItemStock sale2VariantStock = assertCurrentStockValue(testVariantAA, testDefaultStock, 9);
        // Stock entry parent should be the initial stock entry
        ItemStock previousItemStock2 = sale2VariantStock.getPreviousItemStock();
        Assert.assertEquals(initialItemStock, previousItemStock2);

        // Close the first Sale
        saleService.closeSale(testSale);
        // Stock should contains 7 units
        ItemStock sale1VariantStock = assertCurrentStockValue(testVariantAA, testDefaultStock, 7);
        ItemStock previousItemStock = sale1VariantStock.getPreviousItemStock();
        // Parent should be previousItemStock2
        Assert.assertEquals(previousItemStock, sale2VariantStock);

        // Reopen the second sale
        saleService.reopenSale(testSale2);
        // Stock should be back to 8
        ItemStock currentVariantStock = assertCurrentStockValue(testVariantAA, testDefaultStock, 8);
        // And its parent entry should be the initial one
        ItemStock previousItemStock1 = currentVariantStock.getPreviousItemStock();
        Assert.assertEquals(previousItemStock1, initialItemStock);

        // Reopen the first sale
        saleService.reopenSale(testSale);
        // Stock should be back at 10
        assertCurrentStockValue(testVariantAA, testDefaultStock, 10);

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

    public void createTestData() {
        // Country
        Country country = new Country();
        country.setDefaultVatRate(BigDecimal.valueOf(0.21));
        country.setCode("be");
        Country managedCountry = countryService.saveCountry(country);

        // Company
        LocaleText companyName = localeTextFactory.createLocaleText();
        companyName.put(Locale.FRENCH, "Test");
        Company company = new Company();
        company.setCustomerLoyaltyRate(BigDecimal.valueOf(0.1));
        company.setName(companyName);
        company.setCountry(managedCountry);

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("test");
        testEmployee.setLastName("Poe");
        testEmployee.setLocale(Locale.FRENCH);
        testEmployee.setLogin("test");

        Company managedCompany = registrationService.register(company, testEmployee, "test");
        testCompany = managedCompany;


        // Check default stock presence
        StockSearch stockSearch = new StockSearch();
        stockSearch.setCompany(testCompany);
        stockSearch.setActive(true);
        List<Stock> stocks = stockService.findStocks(stockSearch, null);
        Assert.assertFalse("Default stock created", stocks.isEmpty());
        testDefaultStock = stocks.get(0);

        // Item A
        LocaleText itemAName = localeTextFactory.createLocaleText();
        itemAName.put(Locale.FRENCH, "A");
        LocaleText itemADesc = localeTextFactory.createLocaleText();
        itemADesc.put(Locale.FRENCH, "A long description");
        Price itemAPrice = new Price();
        itemAPrice.setVatExclusive(BigDecimal.valueOf(9.99));
        itemAPrice.setVatRate(country.getDefaultVatRate());
        Item itemA = new Item();
        itemA.setCompany(managedCompany);
        itemA.setName(itemAName);
        itemA.setDescription(itemADesc);
        itemA.setActive(true);
        itemA.setCurrentPrice(itemAPrice);
        itemA.setReference("A");
        itemA.setMultipleSale(false);
        Item managedItemA = stockService.saveItem(itemA);

        // Should have a default variant set
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCompany(testCompany);
        ItemVariantSearch itemVariantSearch = new ItemVariantSearch();
        itemVariantSearch.setItem(managedItemA);
        itemVariantSearch.setItemSearch(itemSearch);

        List<ItemVariant> itemVariants = stockService.findItemVariants(itemVariantSearch, null);
        Assert.assertFalse(itemVariants.isEmpty());
        ItemVariant variantADefault = itemVariants.get(0);
        this.testVariantADefault = variantADefault;

        // Variant A-A
        ItemVariant variantAA = new ItemVariant();
        variantAA.setActive(true);
        variantAA.setItem(managedItemA);
        variantAA.setPricing(Pricing.ADD_TO_BASE);
        variantAA.setPricingAmount(BigDecimal.valueOf(5)); // 14.99
        variantAA.setVariantReference("A");
        ItemVariant managedVariantAA = stockService.saveItemVariant(variantAA);
        this.testVariantAA = managedVariantAA;

        // Item B
        LocaleText itemBName = localeTextFactory.createLocaleText();
        itemBName.put(Locale.FRENCH, "itemB");
        LocaleText itemBDesc = localeTextFactory.createLocaleText();
        itemBDesc.put(Locale.FRENCH, "A long description for B");
        Price itemBPrice = new Price();
        itemBPrice.setVatExclusive(BigDecimal.valueOf(9.99));
        itemBPrice.setVatRate(country.getDefaultVatRate());
        Item itemB = new Item();
        itemB.setCompany(managedCompany);
        itemB.setName(itemBName);
        itemB.setDescription(itemBDesc);
        itemB.setActive(true);
        itemB.setMultipleSale(true);
        itemB.setCurrentPrice(itemBPrice);
        itemB.setReference("itemB");
        itemB.setMultipleSale(false);
        Item managedItemB = stockService.saveItem(itemB);

        // Default itemB variant
        itemVariantSearch.setItem(managedItemB);
        itemVariants = stockService.findItemVariants(itemVariantSearch, null);
        Assert.assertFalse(itemVariants.isEmpty());
        ItemVariant variantBDefault = itemVariants.get(0);
        this.testVariantBDefault = variantBDefault;
    }

    public Sale createTestSale() {
        Sale sale = new Sale();
        sale.setCompany(testCompany);
        Sale managedSale = saleService.saveSale(sale);
        return managedSale;
    }

    public ItemVariantSale createItemVariantSale(Sale sale, ItemVariant itemVariant) {
        ItemVariantSale variantSale = new ItemVariantSale();
        variantSale.setItemVariant(itemVariant);
        variantSale.setSale(sale);
        variantSale.setQuantity(BigDecimal.ONE);
        ItemVariantSale managetVariantSale = saleService.saveItemSale(variantSale);
        return managetVariantSale;
    }

}
