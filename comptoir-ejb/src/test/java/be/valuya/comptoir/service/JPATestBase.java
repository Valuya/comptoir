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
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.thirdparty.Employee;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Created by cghislai on 02/04/16.
 */

public abstract class JPATestBase {
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
    private CompanyService companyService;
    @EJB
    private SaleService saleService;
    @EJB
    private CountryService countryService;
    @EJB
    private StockService stockService;
    @EJB
    private LocaleTextFactory localeTextFactory;
    @EJB
    private RegistrationService registrationService;
    @PersistenceContext
    private EntityManager entityManager;

    protected Country country;
    protected Company company;
    protected Stock defaultStock;

    @Before
    public void createData() {
        Assert.assertNotNull(companyService);
        Long countCompanies = companyService.countCompanies();
        Assert.assertEquals("No companies yet", (long) countCompanies, 0L);

        // Country
        Country country = new Country();
        country.setDefaultVatRate(BigDecimal.valueOf(0.21));
        country.setCode("be");
        Country managedCountry = countryService.saveCountry(country);
        this.country =managedCountry;

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
        this.company = managedCompany;

        countCompanies = companyService.countCompanies();
        Assert.assertEquals("Company created", (long) countCompanies, 1L);

        // Check default stock presence
        StockSearch stockSearch = new StockSearch();
        stockSearch.setCompany(this.company);
        stockSearch.setActive(true);
        List<Stock> stocks = stockService.findStocks(stockSearch, null);
        Assert.assertFalse("Default stock created", stocks.isEmpty());
        this.defaultStock = stocks.get(0);
    }

    @After
    public void dropData() {
        // FIXME: Eclipselink code
        ServerSession serverSession = entityManager.unwrap(JpaEntityManager.class).getServerSession();
        SchemaManager schemaManager = new SchemaManager(serverSession);
        schemaManager.dropDefaultTables();
        schemaManager.createDefaultTables(true);
    }

    protected Item createItem(String name, String reference, BigDecimal priceValue) {
        LocaleText nameText = localeTextFactory.createLocaleText();
        nameText.put(Locale.FRENCH, name);
        LocaleText descriptionText = localeTextFactory.createLocaleText();
        BigDecimal defaultVatRate = country.getDefaultVatRate();
        Price price = new Price();
        price.setVatRate(defaultVatRate);
        price.setVatExclusive(priceValue);

        Item item = new Item();
        item.setActive(true);
        item.setDescription(descriptionText);
        item.setReference(reference);
        item.setMultipleSale(false);
        item.setCompany(company);
        item.setCurrentPrice(price);
        item.setName(nameText);
        Item managedItem = stockService.saveItem(item);
        return managedItem;
    }

    protected ItemVariant getDefaultVariant(Item item) {
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCompany(company);
        ItemVariantSearch itemVariantSearch = new ItemVariantSearch();
        itemVariantSearch.setItem(item);
        itemVariantSearch.setItemSearch(itemSearch);
        itemVariantSearch.setVariantReference("default");

        List<ItemVariant> itemVariants = stockService.findItemVariants(itemVariantSearch, null);
        Assert.assertFalse(itemVariants.isEmpty());
        ItemVariant itemVariant = itemVariants.get(0);
        return itemVariant;
    }

    protected ItemVariant createVariant(Item item, String ref) {
        ItemVariant itemVariant = new ItemVariant();
        itemVariant.setActive(true);
        itemVariant.setVariantReference(ref);
        itemVariant.setPricing(Pricing.PARENT_ITEM);
        itemVariant.setItem(item);
        ItemVariant managedVariant = stockService.saveItemVariant(itemVariant);
        return managedVariant;
    }


    public Sale createSale() {
        Sale sale = new Sale();
        sale.setCompany(company);
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
