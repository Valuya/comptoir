package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.ExternalEntity;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.prestashop.domain.Tables;
import be.valuya.comptoir.prestashop.domain.tables.records.PsLangRecord;
import be.valuya.comptoir.prestashop.domain.tables.records.PsProductAttributeCombinationRecord;
import be.valuya.comptoir.prestashop.domain.tables.records.PsProductAttributeRecord;
import be.valuya.comptoir.prestashop.domain.tables.records.PsProductLangRecord;
import be.valuya.comptoir.prestashop.domain.tables.records.PsProductRecord;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TableField;
import org.jooq.impl.DSL;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class PrestashopImportUtil {

    private final ExternalEntityStore<Long, Item> itemStore = new ExternalEntityStore<>();
    private final ExternalEntityStore<Long, ItemVariant> itemVariantStore = new ExternalEntityStore<>();
    private final ExternalEntityStore<Long, AttributeDefinition> attributeDefinitionStore = new ExternalEntityStore<>();
    private final ExternalEntityStore<Long, AttributeValue> attributeValueStore = new ExternalEntityStore<>();
    private final ExternalEntityStore<Long, Locale> localeStore = new ExternalEntityStore<>();
    private final List<ItemVariant> defaultItemVariants = new ArrayList<>();
    private final Company company;
    private final PrestashopImportParams prestashopImportParams;
    private Connection connection;

    public PrestashopImportUtil(Company company, PrestashopImportParams prestashopImportParams) {
        this.company = company;
        this.prestashopImportParams = prestashopImportParams;
    }

    public void importAll() {
        try (Connection newConnection = getConnection()) {
            this.connection = newConnection;
            importLangs();
            importAttributeDefinitions();
            importAttributeValues();
            importItems();
            importItemTexts();
            importItemVariants();
            addDefaultItemVariants();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void importAttributeDefinitions() throws SQLException {
        // AttributeDefinitions = ps_attribute_group in Prestashop
        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        create.select(Tables.PS_ATTRIBUTE_GROUP.ID_ATTRIBUTE_GROUP, Tables.PS_ATTRIBUTE_GROUP_LANG.ID_LANG, Tables.PS_ATTRIBUTE_GROUP_LANG.NAME)
                .from(Tables.PS_ATTRIBUTE_GROUP)
                .leftOuterJoin(Tables.PS_ATTRIBUTE_GROUP_LANG)
                .on(Tables.PS_ATTRIBUTE_GROUP.ID_ATTRIBUTE_GROUP.eq(Tables.PS_ATTRIBUTE_GROUP_LANG.ID_ATTRIBUTE_GROUP))
                .fetch()
                .forEach(record -> {
                    long idAttributeGroup = record.getValue(Tables.PS_ATTRIBUTE_GROUP.ID_ATTRIBUTE_GROUP);
                    long idLang = record.getValue(Tables.PS_ATTRIBUTE_GROUP_LANG.ID_LANG);
                    String localizedName = record.getValue(Tables.PS_ATTRIBUTE_GROUP_LANG.NAME);

                    AttributeDefinition attributeDefinition = attributeDefinitionStore.computeIfAbsent(idAttributeGroup, id -> createAttributeDefinition());

                    Locale locale = localeStore.get(idLang);
                    attributeDefinition.getName().put(locale, localizedName);
                });

    }

    public void importAttributeValues() throws SQLException {
        // AttributeDefinitions = ps_attribute_group in Prestashop
        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        create.select(Tables.PS_ATTRIBUTE.ID_ATTRIBUTE, Tables.PS_ATTRIBUTE.ID_ATTRIBUTE_GROUP, Tables.PS_ATTRIBUTE_LANG.ID_LANG, Tables.PS_ATTRIBUTE_LANG.NAME)
                .from(Tables.PS_ATTRIBUTE)
                .leftOuterJoin(Tables.PS_ATTRIBUTE_LANG)
                .on(Tables.PS_ATTRIBUTE.ID_ATTRIBUTE.eq(Tables.PS_ATTRIBUTE_LANG.ID_ATTRIBUTE))
                .fetch()
                .forEach(record -> {
                    long idAttributeGroup = record.getValue(Tables.PS_ATTRIBUTE.ID_ATTRIBUTE_GROUP);
                    long idAttribute = record.getValue(Tables.PS_ATTRIBUTE.ID_ATTRIBUTE);
                    long idLang = record.getValue(Tables.PS_ATTRIBUTE_LANG.ID_LANG);
                    String localizedName = record.getValue(Tables.PS_ATTRIBUTE_LANG.NAME);

                    Locale locale = localeStore.get(idLang);
                    AttributeDefinition attributeDefinition = attributeDefinitionStore.get(idAttributeGroup);
                    AttributeValue attributeValue = attributeValueStore.computeIfAbsent(idAttribute, id -> createAttributeValue(attributeDefinition));
                    attributeValue.getValue().put(locale, localizedName);
                });
    }

    public void importItems() throws SQLException {
        // AttributeDefinitions = ps_attribute_group in Prestashop
        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        TableField<PsProductRecord, String> referenceField = Tables.PS_PRODUCT.REFERENCE;
        TableField<PsProductRecord, BigDecimal> priceField = Tables.PS_PRODUCT.PRICE;
        TableField<PsProductRecord, Long> idProductField = Tables.PS_PRODUCT.ID_PRODUCT;

        create.select(referenceField, priceField, idProductField)
                .from(Tables.PS_PRODUCT)
                .fetch()
                .forEach(record -> {
                    String productReference = record.getValue(referenceField);
                    BigDecimal vatExclusive = record.getValue(priceField);
                    long idProduct = record.getValue(idProductField);

                    Item item = itemStore.computeIfAbsent(idProduct, id -> createItem());
                    item.setReference(productReference);
                    item.getCurrentPrice().setVatExclusive(vatExclusive);
                    item.getCurrentPrice().setVatRate(BigDecimal.valueOf(21, 2)); // TODO: get correct tax rate
                });
    }

    public void importItemVariants() throws SQLException {
        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        TableField<PsProductAttributeRecord, Long> idProductField = Tables.PS_PRODUCT_ATTRIBUTE.ID_PRODUCT;
        TableField<PsProductAttributeCombinationRecord, Long> idAttributeField = Tables.PS_PRODUCT_ATTRIBUTE_COMBINATION.ID_ATTRIBUTE;
        TableField<PsProductAttributeRecord, Long> idProductAttributeField = Tables.PS_PRODUCT_ATTRIBUTE.ID_PRODUCT_ATTRIBUTE;
        TableField<PsProductAttributeRecord, String> referenceField = Tables.PS_PRODUCT_ATTRIBUTE.REFERENCE;
        TableField<PsProductAttributeRecord, BigDecimal> priceField = Tables.PS_PRODUCT_ATTRIBUTE.PRICE;
        create.select(idProductField, idAttributeField, idProductAttributeField, referenceField, priceField)
                .from(Tables.PS_PRODUCT_ATTRIBUTE)
                .leftOuterJoin(Tables.PS_PRODUCT_ATTRIBUTE_COMBINATION)
                .on(idProductAttributeField.eq(Tables.PS_PRODUCT_ATTRIBUTE_COMBINATION.ID_PRODUCT_ATTRIBUTE))
                .fetch()
                .forEach(record -> {
                    Long idProduct = record.getValue(idProductField);
                    Long idAttribute = record.getValue(idAttributeField);
                    Long idProductAttribute = record.getValue(idProductAttributeField);
                    String variantReference = record.getValue(referenceField);
                    if (variantReference == null) {
                        variantReference = Long.toString(idProductAttribute);
                    }
                    BigDecimal pricingAmount = record.getValue(priceField);

                    AttributeValue attributeValue = attributeValueStore.get(idAttribute);

                    Item item = itemStore.get(idProduct);

                    ItemVariant itemVariant = itemVariantStore.computeIfAbsent(idProductAttribute, id -> createItemVariant(item));
                    if (pricingAmount == null) {
                        itemVariant.setPricing(Pricing.PARENT_ITEM);
                    } else {
                        itemVariant.setPricing(Pricing.ADD_TO_BASE);
                        itemVariant.setPricingAmount(pricingAmount);
                    }
                    List<AttributeValue> attributeValues = itemVariant.getAttributeValues();
                    itemVariant.setVariantReference(variantReference);
                    attributeValues.add(attributeValue);
                });
    }

    public void importItemTexts() throws SQLException {
        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        TableField<PsProductLangRecord, Long> idLangField = Tables.PS_PRODUCT_LANG.ID_LANG;
        TableField<PsProductLangRecord, Long> idProductField = Tables.PS_PRODUCT_LANG.ID_PRODUCT;
        TableField<PsProductLangRecord, String> nameField = Tables.PS_PRODUCT_LANG.NAME;
        TableField<PsProductLangRecord, String> descriptionShortField = Tables.PS_PRODUCT_LANG.DESCRIPTION_SHORT;

        create.select(idLangField, idProductField, nameField, descriptionShortField)
                .from(Tables.PS_PRODUCT_LANG)
                .fetch()
                .forEach(record -> {
                    long idLang = record.getValue(idLangField);
                    long idProduct = record.getValue(idProductField);
                    String itemName = record.getValue(nameField);
                    String itemDescription = record.getValue(descriptionShortField);

                    Item item = itemStore.get(idProduct);
                    Locale locale = localeStore.get(idLang);

                    LocaleText name = item.getName();
                    name.put(locale, itemName);

                    LocaleText description = item.getDescription();
                    description.put(locale, itemDescription);
                });
    }

    private void addDefaultItemVariants() {
        itemStore.stream()
                .map(ExternalEntity::getValue)
                .filter(this::hasItemVariants)
                .map(this::createItemVariant)
                .peek(itemVariant -> {
                    itemVariant.setPricing(Pricing.PARENT_ITEM);
                    itemVariant.setVariantReference("0");
                })
                .forEach(defaultItemVariants::add);
    }

    private boolean hasItemVariants(Item item) {
        return itemVariantStore.stream()
                .map(ExternalEntity::getValue)
                .map(ItemVariant::getItem)
                .anyMatch(item::equals);
    }

    private void importLangs() throws SQLException {
        TableField<PsLangRecord, Long> idLangField = Tables.PS_LANG.ID_LANG;
        TableField<PsLangRecord, String> isoCodeField = Tables.PS_LANG.ISO_CODE;

        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        create.select(idLangField, isoCodeField)
                .from(Tables.PS_LANG)
                .where(Tables.PS_LANG.ACTIVE.isTrue())
                .fetch()
                .forEach(record -> {
                    long idLang = record.getValue(idLangField);
                    String isoCode = record.getValue(isoCodeField);
                    Locale locale = new Locale(isoCode);
                    localeStore.put(idLang, locale);
                });
    }

    private AttributeDefinition createAttributeDefinition() {
        LocaleText localeText = createLocaleText();

        AttributeDefinition attributeDefinition = new AttributeDefinition();
        attributeDefinition.setName(localeText);
        attributeDefinition.setCompany(company);

        return attributeDefinition;
    }

    private AttributeValue createAttributeValue(AttributeDefinition attributeDefinition) {
        LocaleText localeText = createLocaleText();

        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setValue(localeText);
        attributeValue.setAttributeDefinition(attributeDefinition);

        return attributeValue;
    }

    private Item createItem() {
        LocaleText name = createLocaleText();

        LocaleText description = createLocaleText();

        Price price = new Price();

        Item item = new Item();
        item.setCompany(company);
        item.setName(name);
        item.setDescription(description);
        item.setCurrentPrice(price);
        item.setActive(true);

        return item;
    }

    private ItemVariant createItemVariant(Item item) {
        List<AttributeValue> attributeValues = new ArrayList<>();

        ItemVariant itemVariant = new ItemVariant();
        itemVariant.setAttributeValues(attributeValues);
        itemVariant.setItem(item);
        itemVariant.setActive(true);

        return itemVariant;
    }

    private LocaleText createLocaleText() {
        LocaleText localeText = new LocaleText();

        Map<Locale, String> localeTextMap = new HashMap<>();
        localeText.setLocaleTextMap(localeTextMap);

        return localeText;
    }

    public ExternalEntityStore<Long, Item> getItemStore() {
        return itemStore;
    }

    public ExternalEntityStore<Long, ItemVariant> getItemVariantStore() {
        return itemVariantStore;
    }

    public ExternalEntityStore<Long, AttributeDefinition> getAttributeDefinitionStore() {
        return attributeDefinitionStore;
    }

    public ExternalEntityStore<Long, AttributeValue> getAttributeValueStore() {
        return attributeValueStore;
    }

    public List<ItemVariant> getDefaultItemVariants() {
        return defaultItemVariants;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String driverClassName = prestashopImportParams.getDriverClassName();
        Class<?> driverClass = Class.forName(driverClassName);
        driverClass.newInstance();

        String database = prestashopImportParams.getDatabase();
        String username = prestashopImportParams.getUsername();
        String password = prestashopImportParams.getPassword();
        String host = prestashopImportParams.getHost();
        int port = prestashopImportParams.getPort();

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", username);
        connectionProperties.put("password", password);

        Connection newConnection = DriverManager.getConnection(
                "jdbc:mysql://"
                + host
                + ":" + port + "/" + database,
                connectionProperties);
        return newConnection;
    }
}
