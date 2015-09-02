package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.ItemPicture_;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariant_;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.lang.LocaleText_;
import be.valuya.comptoir.model.search.AttributeSearch;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.ItemStock_;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.stock.Stock_;
import be.valuya.comptoir.util.pagination.AttributeDefinitionColumn;
import be.valuya.comptoir.util.pagination.ItemVariantColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class StockService {

    @PersistenceContext
    private EntityManager entityManager;

    @Nonnull
    public List<ItemVariant> findItems(@Nonnull ItemSearch itemSearch, @CheckForNull Pagination<ItemVariant, ItemVariantColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemVariant> query = criteriaBuilder.createQuery(ItemVariant.class);
        Root<ItemVariant> itemVariantRoot = query.from(ItemVariant.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = itemSearch.getCompany();
        Join<ItemVariant, Item> itemJoin = itemVariantRoot.join(ItemVariant_.item, JoinType.LEFT);

        Path<Company> companyPath = itemJoin.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Path<String> referencePath = itemJoin.get(Item_.reference);

        String multiSearch = itemSearch.getMultiSearch();
        if (multiSearch != null && !multiSearch.isEmpty()) {
            String lowerMultiSearch = multiSearch.toLowerCase(Locale.FRENCH); //TODO: actual locale

            Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemJoin, lowerMultiSearch);
            Predicate variantReferenceContainsPredicate = createVariantReferenceContainsPredicate(itemVariantRoot, lowerMultiSearch);
            Predicate namePredicate = createNameContainsPredicate(itemJoin, lowerMultiSearch);
            Predicate descriptionPredicate = createDescriptionContainsPredicate(itemJoin, lowerMultiSearch);

            Predicate multiSearchPredicate = criteriaBuilder.or(referenceContainsPredicate, variantReferenceContainsPredicate, namePredicate, descriptionPredicate);
            predicates.add(multiSearchPredicate);
        }

        String reference = itemSearch.getReference();
        if (reference != null && !reference.trim().isEmpty()) {
            reference = reference.trim();
            Predicate referencePredicate = criteriaBuilder.equal(referencePath, reference);
            predicates.add(referencePredicate);
        }

        String referenceContains = itemSearch.getReferenceContains();
        if (referenceContains != null && !referenceContains.trim().isEmpty()) {
            referenceContains = referenceContains.trim().toLowerCase(Locale.FRENCH);
            Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemJoin, referenceContains);
            predicates.add(referenceContainsPredicate);
        }

        String variantReferenceContains = itemSearch.getVariantReferenceContains();
        if (variantReferenceContains != null && !variantReferenceContains.trim().isEmpty()) {
            variantReferenceContains = variantReferenceContains.trim().toLowerCase(Locale.FRENCH);
            Predicate variantReferenceContainsPredicate = createVariantReferenceContainsPredicate(itemVariantRoot, variantReferenceContains);
            predicates.add(variantReferenceContainsPredicate);
        }

        String model = itemSearch.getVariantReference();
        if (model != null && !model.trim().isEmpty()) {
            model = model.trim();
            Path<String> modelPath = itemVariantRoot.get(ItemVariant_.variantReference);
            Predicate modelPredicate = criteriaBuilder.equal(modelPath, model);
            predicates.add(modelPredicate);
        }

        String nameContains = itemSearch.getNameContains();
        if (nameContains != null && !nameContains.trim().isEmpty()) {
            nameContains = nameContains.trim().toLowerCase(Locale.FRENCH);
            Predicate namePredicate = createNameContainsPredicate(itemJoin, nameContains);
            predicates.add(namePredicate);
        }

        String descriptionContains = itemSearch.getDescriptionContains();
        if (descriptionContains != null && !descriptionContains.trim().isEmpty()) {
            descriptionContains = descriptionContains.trim().toLowerCase(Locale.FRENCH);
            Predicate descriptionPredicate = createDescriptionContainsPredicate(itemJoin, descriptionContains);
            predicates.add(descriptionPredicate);
        }

        if (pagination != null) {
            List<Sort<ItemVariantColumn>> sortings = pagination.getSortings();
            List<Order> orders = ItemVariantColumnPersistenceUtil.createOrdersFromSortings(criteriaBuilder, itemVariantRoot, sortings);
            query.orderBy(orders);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);
        query.distinct(true);

        TypedQuery<ItemVariant> typedQuery = entityManager.createQuery(query);

        if (pagination != null) {
            int offset = pagination.getOffset();
            int maxResults = pagination.getMaxResults();
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(maxResults);
        }

        List<ItemVariant> items = typedQuery.getResultList();

        return items;
    }

    private Predicate createVariantReferenceContainsPredicate(Path<ItemVariant> itemVariantPath, String referenceContains) {
        Path<String> referencePath = itemVariantPath.get(ItemVariant_.variantReference);

        return createContainsPredicate(referencePath, referenceContains);
    }

    private Predicate createReferenceContainsPredicate(Path<Item> itemPath, String referenceContains) {
        Path<String> referencePath = itemPath.get(Item_.reference);

        return createContainsPredicate(referencePath, referenceContains);
    }

    private Predicate createContainsPredicate(Path<String> path, String text) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Expression<String> lowerReferenceExpression = criteriaBuilder.lower(path);
        Expression<Integer> locationExpression = criteriaBuilder.locate(lowerReferenceExpression, text);
        Predicate referenceContainsPredicate = criteriaBuilder.greaterThan(locationExpression, 0);
        return referenceContainsPredicate;
    }

    private Predicate createDescriptionContainsPredicate(From<?, Item> itemPath, String descriptionContains) {
        Join<Item, LocaleText> descriptionJoin = itemPath.join(Item_.description);
        MapJoin<LocaleText, Locale, String> localeTextMapJoin = descriptionJoin.join(LocaleText_.localeTextMap);
        Path<String> textPath = localeTextMapJoin.value();
        Predicate descriptionPredicate = createContainsPredicate(textPath, descriptionContains);
        return descriptionPredicate;
    }

    private Predicate createNameContainsPredicate(From<?, Item> itemPath, String nameContains) {
        Join<Item, LocaleText> nameJoin = itemPath.join(Item_.name);
        MapJoin<LocaleText, Locale, String> localeTextMapJoin = nameJoin.join(LocaleText_.localeTextMap);
        Path<String> textPath = localeTextMapJoin.value();
        Predicate namePredicate = createContainsPredicate(textPath, nameContains);
        return namePredicate;
    }

    public ItemStock adaptStockFromItemSale(ZonedDateTime fromDateTime, Stock stock, ItemSale managedItemSale, StockChangeType stockChangeType, String comment) {
        ItemVariant managedItem = managedItemSale.getItemVariant();
        BigDecimal soldQuantity = managedItemSale.getQuantity();

        // find previous stock value
        ItemStock managedPreviousItemStock = findItemStock(managedItem, stock, fromDateTime);

        // handle old values
        BigDecimal oldQuantity;
        if (managedPreviousItemStock != null) {
            oldQuantity = managedPreviousItemStock.getQuantity();
        } else {
            oldQuantity = BigDecimal.ZERO;
        }

        // adapt quantities
        BigDecimal newQuantity = oldQuantity.subtract(soldQuantity);

        return adaptStock(fromDateTime, stock, managedItem, newQuantity, comment);
    }

    /**
     * Adapt stock values by creating a new ItemStock and updating the previous one.
     *
     * @param fromDateTime
     * @param stock
     * @param managedItem
     * @param newQuantity
     * @param comment
     * @return
     */
    @Nonnull
    public ItemStock adaptStock(@Nonnull ZonedDateTime fromDateTime, @Nonnull Stock stock, @Nonnull ItemVariant managedItem, @Nonnull BigDecimal newQuantity, @CheckForNull String comment) {
        // create new stock value
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(stock);
        itemStock.setItemVariant(managedItem);
        itemStock.setStartDateTime(fromDateTime);
        itemStock.setQuantity(newQuantity);
        itemStock.setComment(comment);

        // persist
        ItemStock managedItemStock = entityManager.merge(itemStock);

        return managedItemStock;
    }

    @Nonnull
    public List<ItemStock> findItemStocks(@Nonnull ItemStockSearch itemStockSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemStock> query = criteriaBuilder.createQuery(ItemStock.class);
        Root<ItemStock> itemStockRoot = query.from(ItemStock.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = itemStockSearch.getCompany();
        Join<ItemStock, ItemVariant> itemVariantJoin = itemStockRoot.join(ItemStock_.itemVariant, JoinType.LEFT);
        Join<ItemVariant, Item> itemJoin = itemVariantJoin.join(ItemVariant_.item, JoinType.LEFT);
        Path<Company> companyPath = itemJoin.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Stock stock = itemStockSearch.getStock();
        if (stock != null) {
            Path<Stock> stockPath = itemStockRoot.get(ItemStock_.stock);
            Predicate stockPredicate = criteriaBuilder.equal(stockPath, stock);
            predicates.add(stockPredicate);
        }

        ItemVariant itemVariant = itemStockSearch.getItemVariant();
        if (itemVariant != null) {
            Predicate itemPredicate = criteriaBuilder.equal(itemJoin, itemVariant);
            predicates.add(itemPredicate);
        }

        ZonedDateTime atDateTime = itemStockSearch.getAtDateTime();
        if (atDateTime != null) {
            Path<ZonedDateTime> startDateTimePath = itemStockRoot.get(ItemStock_.startDateTime);
            Predicate startDateTimePredicate = criteriaBuilder.lessThanOrEqualTo(startDateTimePath, atDateTime);
            predicates.add(startDateTimePredicate);
        }

        Path<ZonedDateTime> toDateTimePath = itemStockRoot.get(ItemStock_.endDateTime);
        Predicate noEndPredicate = criteriaBuilder.isNull(toDateTimePath);
        Predicate endAfterPredicate = criteriaBuilder.lessThan(toDateTimePath, atDateTime);
        Predicate endDateTimePredicate = criteriaBuilder.or(noEndPredicate, endAfterPredicate);
        predicates.add(endDateTimePredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<ItemStock> typedQuery = entityManager.createQuery(query);

        return typedQuery.getResultList();
    }

    @Nonnull
    public List<ItemStock> findItemStocks(@Nonnull ItemVariant itemVariant, @Nonnull ZonedDateTime atDateTime) {
        Item item = itemVariant.getItem();
        Company company = item.getCompany();
        ItemStockSearch itemStockSearch = new ItemStockSearch();
        itemStockSearch.setCompany(company);
        itemStockSearch.setItemVariant(itemVariant);
        itemStockSearch.setAtDateTime(atDateTime);

        return findItemStocks(itemStockSearch);
    }

    @Nonnull
    public List<ItemStock> findItemStocks(ItemVariant item) {
        ZonedDateTime dateTime = ZonedDateTime.now();
        return findItemStocks(item, dateTime);
    }

    /**
     * Find ItemStock at given time for item, in given Stock.
     *
     * @param itemVariant
     * @param stock
     * @param atDateTime
     * @return
     */
    @CheckForNull
    public ItemStock findItemStock(@Nonnull ItemVariant itemVariant, @Nonnull Stock stock, @Nonnull ZonedDateTime atDateTime) {
        Item item = itemVariant.getItem();
        Company company = item.getCompany();
        ItemStockSearch itemStockSearch = new ItemStockSearch();
        itemStockSearch.setCompany(company);
        itemStockSearch.setStock(stock);
        itemStockSearch.setItemVariant(itemVariant);
        itemStockSearch.setAtDateTime(atDateTime);

        List<ItemStock> itemStocks = findItemStocks(itemStockSearch);
        if (itemStocks.isEmpty()) {
            return null;
        }
        if (itemStocks.size() > 1) {
            String errorMessage = MessageFormat.format("Multiple stock entries for item {0} in stock {1} at {2}", itemVariant, stock, atDateTime);
            throw new AssertionError(errorMessage);
        }
        return itemStocks.get(0);
    }

    @Nonnull
    public List<Stock> findStocks(@Nonnull Company company) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = criteriaBuilder.createQuery(Stock.class);
        Root<Stock> stockRoot = query.from(Stock.class);

        Path<Company> companyPath = stockRoot.get(Stock_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);

        query.where(companyPredicate);

        TypedQuery<Stock> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public ItemVariant saveItem(ItemVariant item, Stock stock, BigDecimal initialQuantity) {
        ItemVariant managedItem = entityManager.merge(item);
        ZonedDateTime dateTime = ZonedDateTime.now();

        adaptStock(dateTime, stock, managedItem, initialQuantity, null);

        return managedItem;
    }

    public ItemVariant findItemVariantById(Long id) {
        return entityManager.find(ItemVariant.class, id);
    }

    public Item findItemById(Long id) {
        return entityManager.find(Item.class, id);
    }

    public ItemPicture findItemPictureById(Long id) {
        return entityManager.find(ItemPicture.class, id);
    }

    /**
     * DEPRECATED: see method version with params etc., decide what to do.
     *
     * @param item
     * @return
     * @deprecated
     */
    @Deprecated
    public ItemVariant saveItem(ItemVariant item) {
        return entityManager.merge(item);
    }

    public ItemPicture saveItemPicture(ItemPicture itemPicture) {
        ItemVariant itemVariant = itemPicture.getItemVariant();
        ItemPicture managedPicture = entityManager.merge(itemPicture);
        itemVariant.setMainPicture(itemPicture);
        ItemVariant managedItem = entityManager.merge(itemVariant);
        return managedPicture;
    }

    public List<ItemPicture> findItemPictures(@Nonnull ItemVariant item) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemPicture> query = criteriaBuilder.createQuery(ItemPicture.class);
        Root<ItemPicture> itemPictureRoot = query.from(ItemPicture.class);

        List<Predicate> predicates = new ArrayList<>();

        Path<ItemVariant> itemPath = itemPictureRoot.get(ItemPicture_.itemVariant);
        Predicate itemPredicate = criteriaBuilder.equal(itemPath, item);
        predicates.add(itemPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<ItemPicture> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public AttributeDefinition findAttributeDefinitionById(long id) {
        return entityManager.find(AttributeDefinition.class, id);
    }

    public AttributeValue findAttributeValueById(long id) {
        return entityManager.find(AttributeValue.class, id);
    }

    public AttributeDefinition saveAttributeDefinition(AttributeDefinition attributeDefinition) {
        return entityManager.merge(attributeDefinition);
    }

    public AttributeValue saveAttributeValue(AttributeValue attributeValue) {
        return entityManager.merge(attributeValue);
    }

    public List<AttributeDefinition> findAttributeDefinitions(AttributeSearch attributeSearch, Pagination<AttributeDefinition, AttributeDefinitionColumn> pagination) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
