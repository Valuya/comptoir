package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.ExternalEntity;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

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
            loadLangs();
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT \n"
                + "    ag.id_attribute_group,\n"
                + "    agl.id_lang,\n"
                + "    agl.name\n"
                + "FROM\n"
                + "    ps_attribute_group ag\n"
                + "        LEFT JOIN\n"
                + "    ps_attribute_group_lang agl ON ag.id_attribute_group = agl.id_attribute_group;"
        )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long idAttributeGroup = resultSet.getLong("ag.id_attribute_group");
                    long idLang = resultSet.getLong("agl.id_lang");
                    String localizedName = resultSet.getString("agl.name");

                    AttributeDefinition attributeDefinition = attributeDefinitionStore.computeIfAbsent(idAttributeGroup, id -> createAttributeDefinition());

                    Locale locale = localeStore.get(idLang);
                    attributeDefinition.getName().put(locale, localizedName);
                }
            }
        }

    }

    public void importAttributeValues() throws SQLException {
        // AttributeDefinitions = ps_attribute_group in Prestashop
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT \n"
                + "    a.id_attribute,\n"
                + "    a.id_attribute_group,\n"
                + "    al.id_lang,\n"
                + "    al.name\n"
                + "FROM\n"
                + "    ps_attribute a\n"
                + "        LEFT JOIN\n"
                + "    ps_attribute_lang al ON a.id_attribute = al.id_attribute;"
        )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long idAttributeGroup = resultSet.getLong("a.id_attribute_group");
                    long idAttribute = resultSet.getLong("a.id_attribute");
                    long idLang = resultSet.getLong("al.id_lang");
                    String localizedName = resultSet.getString("al.name");

                    Locale locale = localeStore.get(idLang);
                    AttributeDefinition attributeDefinition = attributeDefinitionStore.get(idAttributeGroup);
                    AttributeValue attributeValue = attributeValueStore.computeIfAbsent(idAttribute, id -> createAttributeValue(attributeDefinition));
                    attributeValue.getValue().put(locale, localizedName);
                }
            }
        }

    }

    public void importItems() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT \n"
                + "    p.id_product,\n"
                + "    p.price,\n"
                + "    p.reference\n"
                + "FROM\n"
                + "    ps_product p;"
        )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productReference = resultSet.getString("p.reference");
                    BigDecimal vatExclusive = resultSet.getBigDecimal("p.price");
                    Long idProduct = resultSet.getLong("p.id_product");

                    Item item = itemStore.computeIfAbsent(idProduct, id -> createItem());
                    item.setReference(productReference);
                    item.getCurrentPrice().setVatExclusive(vatExclusive);
                    item.getCurrentPrice().setVatRate(BigDecimal.valueOf(21, 2)); // TODO: get correct tax rate
                }
            }
        }

    }

    public void importItemVariants() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT \n"
                + "    pa.id_product,\n"
                + "    pac.id_product_attribute,\n"
                + "    pac.id_attribute,\n"
                + "    pa.reference\n"
                + "FROM\n"
                + "    ps_product_attribute pa\n"
                + "        LEFT JOIN\n"
                + "    ps_product_attribute_combination pac ON pa.id_product_attribute = pac.id_product_attribute;"
        )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String variantReference = resultSet.getString("pa.reference");
                    Long idProduct = resultSet.getLong("pa.id_product");
                    Long idAttribute = resultSet.getLong("pac.id_attribute");
                    Long idProductAttribute = resultSet.getLong("pac.id_product_attribute");

                    AttributeValue attributeValue = attributeValueStore.get(idAttribute);

                    Item item = itemStore.get(idProduct);

                    ItemVariant itemVariant = itemVariantStore.computeIfAbsent(idProductAttribute, id -> createItemVariant(item));
                    List<AttributeValue> attributeValues = itemVariant.getAttributeValues();
                    itemVariant.setVariantReference(variantReference);
                    attributeValues.add(attributeValue);
                }
            }
        }

    }

    public void importItemTexts() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from ps_product_lang")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long idLang = resultSet.getLong("id_lang");
                    long idProduct = resultSet.getLong("id_product");
                    String itemName = resultSet.getString("name");
                    String itemDescription = resultSet.getString("description_short");

                    Item item = itemStore.get(idProduct);
                    Locale locale = localeStore.get(idLang);

                    LocaleText name = item.getName();
                    name.put(locale, itemName);

                    LocaleText description = item.getDescription();
                    description.put(locale, itemDescription);
                }
            }
        }
    }

    private void addDefaultItemVariants() {
        itemStore.stream()
                .map(ExternalEntity::getValue)
                .filter(this::hasItemVariants)
                .map(this::createItemVariant)
                .forEach(defaultItemVariants::add);
    }

    private boolean hasItemVariants(Item item) {
        return itemVariantStore.stream()
                .map(ExternalEntity::getValue)
                .map(ItemVariant::getItem)
                .anyMatch(item::equals);
    }

    private void loadLangs() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from ps_lang where active")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long idLang = resultSet.getLong("id_lang");
                    String isoCode = resultSet.getString("iso_code");
                    Locale locale = new Locale(isoCode);
                    localeStore.put(idLang, locale);
                }
            }
        }
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

        return item;
    }

    private ItemVariant createItemVariant(Item item) {
        List<AttributeValue> attributeValues = new ArrayList<>();

        ItemVariant itemVariant = new ItemVariant();
        itemVariant.setAttributeValues(attributeValues);
        itemVariant.setItem(item);

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

}
