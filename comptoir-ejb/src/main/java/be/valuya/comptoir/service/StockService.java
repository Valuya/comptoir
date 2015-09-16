package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeDefinition_;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.AttributeValue_;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.ItemPicture_;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantPicture;
import be.valuya.comptoir.model.commercial.ItemVariantPicture_;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariant_;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.commercial.Picture_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.lang.LocaleText_;
import be.valuya.comptoir.model.search.AttributeSearch;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.search.ItemVariantSearch;
import be.valuya.comptoir.model.search.PictureSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.ItemStock_;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.stock.Stock_;
import be.valuya.comptoir.util.pagination.AttributeDefinitionColumn;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.ItemVariantColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.EJB;
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class StockService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PaginatedQueryService paginatedQueryService;

    @Nonnull
    public List<Item> findItems(@Nonnull ItemSearch itemSearch, @CheckForNull Pagination<Item, ItemColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = criteriaBuilder.createQuery(Item.class);
        Root<Item> itemRoot = query.from(Item.class);

        List<Predicate> predicates = createItemPredicates(itemSearch, itemRoot, null);

        paginatedQueryService.applySort(pagination, itemRoot, query,
                itemColumn -> ItemColumnPersistenceUtil.getPath(itemRoot, itemColumn)
        );

        List<Item> items = paginatedQueryService.getResults(predicates, query, itemRoot, pagination);

        return items;
    }

    @Nonnull
    public List<ItemVariant> findItemVariants(@Nonnull ItemVariantSearch itemVariantSearch, @CheckForNull Pagination<ItemVariant, ItemVariantColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemVariant> query = criteriaBuilder.createQuery(ItemVariant.class);
        Root<ItemVariant> itemVariantRoot = query.from(ItemVariant.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<ItemVariant, Item> itemJoin = itemVariantRoot.join(ItemVariant_.item, JoinType.LEFT);

        List<Predicate> itemPredicates = createItemVariantPredicates(itemVariantSearch, itemJoin, itemVariantRoot);
        predicates.addAll(itemPredicates);

        paginatedQueryService.applySort(pagination, itemVariantRoot, query,
                itemVariantColumn -> ItemVariantColumnPersistenceUtil.getPath(itemVariantRoot, itemVariantColumn)
        );

        query.distinct(true);

        List<ItemVariant> items = paginatedQueryService.getResults(predicates, query, itemVariantRoot, pagination);

        return items;
    }

    private void paginate(Pagination<?, ?> pagination, TypedQuery<?> typedQuery) {
        if (pagination != null) {
            int offset = pagination.getOffset();
            int maxResults = pagination.getMaxResults();
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(maxResults);
        }
    }

    private List<Predicate> createItemVariantPredicates(ItemVariantSearch itemVariantSearch, From<?, Item> itemFrom, From<?, ItemVariant> itemVariantFrom) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        ItemSearch itemSearch = itemVariantSearch.getItemSearch();
        List<Predicate> predicates = createItemPredicates(itemSearch, itemFrom, itemVariantFrom);

        
        Path<Boolean> activePath = itemVariantFrom.get(ItemVariant_.active);
        Predicate activePredicate = criteriaBuilder.equal(activePath, Boolean.TRUE);
        predicates.add(activePredicate);
        
        Item item = itemVariantSearch.getItem();
        if (item != null) {
            Predicate itemPredicate = criteriaBuilder.equal(itemFrom, item);
            predicates.add(itemPredicate);
        }
        String variantReference = itemVariantSearch.getVariantReference();
        if (variantReference != null) {
            Path<String> variantReferencePath = itemVariantFrom.get(ItemVariant_.variantReference);
            Predicate variantReferencePredicate = criteriaBuilder.equal(variantReferencePath, variantReference);
            predicates.add(variantReferencePredicate);
        }
        String variantReferenceContains = itemVariantSearch.getVariantReferenceContains();
        if (variantReferenceContains != null) {
            Predicate createVariantReferenceContainsPredicate = createVariantReferenceContainsPredicate(itemVariantFrom, variantReferenceContains);
            predicates.add(createVariantReferenceContainsPredicate);
        }
        return predicates;
    }
    
    private List<Predicate> createItemPredicates(ItemSearch itemSearch, From<?, Item> itemFrom, From<?, ItemVariant> itemVariantFrom) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        List<Predicate> predicates = new ArrayList<>();
        
        Path<Boolean> activePath = itemFrom.get(Item_.active);
        Predicate activePredicate = criteriaBuilder.equal(activePath, Boolean.TRUE);
        predicates.add(activePredicate);
        
        Company company = itemSearch.getCompany();
        Path<Company> companyPath = itemFrom.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);
        
        Path<String> referencePath = itemFrom.get(Item_.reference);
        String multiSearch = itemSearch.getMultiSearch();
        if (multiSearch != null && !multiSearch.isEmpty()) {
            Predicate multiSearchPredicate = createItemMultiSearchPredicate(multiSearch, itemFrom, itemVariantFrom, criteriaBuilder);
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
            Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemFrom, referenceContains);
            predicates.add(referenceContainsPredicate);
        }

        String nameContains = itemSearch.getNameContains();
        if (nameContains != null && !nameContains.trim().isEmpty()) {
            nameContains = nameContains.trim().toLowerCase(Locale.FRENCH);
            Predicate namePredicate = createItemNameContainsPredicate(itemFrom, nameContains);
            predicates.add(namePredicate);
        }

        String descriptionContains = itemSearch.getDescriptionContains();
        if (descriptionContains != null && !descriptionContains.trim().isEmpty()) {
            descriptionContains = descriptionContains.trim().toLowerCase(Locale.FRENCH);
            Predicate descriptionPredicate = createDescriptionContainsPredicate(itemFrom, descriptionContains);
            predicates.add(descriptionPredicate);
        }

        return predicates;
    }

    private Predicate createItemMultiSearchPredicate(String multiSearch, From<?, Item> itemFrom, From<?, ItemVariant> itemVariantFrom, CriteriaBuilder criteriaBuilder) {
        String lowerMultiSearch = multiSearch.toLowerCase(Locale.FRENCH); //TODO: actual locale
        List<Predicate> multiSearchPredicates = new ArrayList<>();
        Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemFrom, lowerMultiSearch);
        multiSearchPredicates.add(referenceContainsPredicate);
        Predicate namePredicate = createItemNameContainsPredicate(itemFrom, lowerMultiSearch);
        multiSearchPredicates.add(namePredicate);
        Predicate descriptionPredicate = createDescriptionContainsPredicate(itemFrom, lowerMultiSearch);
        multiSearchPredicates.add(descriptionPredicate);
        if (itemVariantFrom != null) {
            Predicate variantReferenceContainsPredicate = createVariantReferenceContainsPredicate(itemVariantFrom, lowerMultiSearch);
            multiSearchPredicates.add(variantReferenceContainsPredicate);
        }
        // combine
        Predicate[] multiSearchPredicateArray = multiSearchPredicates.toArray(new Predicate[0]);
        Predicate multiSearchPredicate = criteriaBuilder.or(multiSearchPredicateArray);
        return multiSearchPredicate;
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
        Predicate descriptionPredicate = createLocaleTextContainsPredicate(descriptionJoin, descriptionContains);
        return descriptionPredicate;
    }

    private Predicate createItemNameContainsPredicate(From<?, Item> itemPath, String nameContains) {
        Join<Item, LocaleText> nameJoin = itemPath.join(Item_.name);
        Predicate namePredicate = createLocaleTextContainsPredicate(nameJoin, nameContains);
        return namePredicate;
    }

    private Predicate createAttributeDefinitionNameContainsPredicate(From<?, AttributeDefinition> attributeDefinitionPath, String contains) {
        Join<AttributeDefinition, LocaleText> nameJoin = attributeDefinitionPath.join(AttributeDefinition_.name);
        Predicate namePredicate = createLocaleTextContainsPredicate(nameJoin, contains);
        return namePredicate;
    }

    private Predicate createAttributeValueContainsPredicate(From<?, AttributeValue> attributeValuePath, String contains) {
        Join<AttributeValue, LocaleText> valueJoin = attributeValuePath.join(AttributeValue_.value);
        Predicate namePredicate = createLocaleTextContainsPredicate(valueJoin, contains);
        return namePredicate;
    }

    private Predicate createLocaleTextContainsPredicate(From<?, LocaleText> localeTextPath, String contains) {
        MapJoin<LocaleText, Locale, String> localeTextMapJoin = localeTextPath.join(LocaleText_.localeTextMap);
        Path<String> textPath = localeTextMapJoin.value();
        Predicate namePredicate = createContainsPredicate(textPath, contains);
        return namePredicate;
    }

    public ItemStock adaptStockFromItemSale(ZonedDateTime fromDateTime, Stock stock, ItemVariantSale managedItemSale, StockChangeType stockChangeType, String comment) {
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
     * Adapt stock values by creating a new ItemStock and updating the previous
     * one.
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

    public Picture findPictureById(Long id) {
        return entityManager.find(Picture.class, id);
    }

    /**
     * DEPRECATED: see method version with params etc., decide what to do.
     *
     * @param item
     * @return
     * @deprecated
     */
    @Deprecated
    public ItemVariant saveItemVariant(ItemVariant item) {
        return entityManager.merge(item);
    }

    @Deprecated
    public Item saveItem(Item item) {
        return entityManager.merge(item);
    }

    public Picture savePicture(Picture picture) {
        Picture managedPicture = entityManager.merge(picture);
        return managedPicture;
    }

    public List<Picture> findPictures(@Nonnull PictureSearch pictureSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Picture> query = criteriaBuilder.createQuery(Picture.class);
        Root<Picture> pictureRoot = query.from(Picture.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = pictureSearch.getCompany();
        Path<Company> companyPath = pictureRoot.get(Picture_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        ItemVariant itemVariant = pictureSearch.getItemVariant();
        if (itemVariant != null) {
            Subquery<ItemVariantPicture> itemVariantPictureSubquery = query.subquery(ItemVariantPicture.class);
            Root<ItemVariantPicture> itemVariantPictureRoot = itemVariantPictureSubquery.from(ItemVariantPicture.class);

            Path<Picture> itemVariantPicturePicturePath = itemVariantPictureRoot.get(ItemVariantPicture_.picture);
            Predicate itemVariantPicturePicturePredicate = criteriaBuilder.equal(itemVariantPicturePicturePath, pictureRoot);

            Path<ItemVariant> itemVariantPictureItemVariantPath = itemVariantPictureRoot.get(ItemVariantPicture_.itemVariant);
            Predicate itemVariantPictureItemVariantPredicate = criteriaBuilder.equal(itemVariantPictureItemVariantPath, pictureRoot);

            itemVariantPictureSubquery.where(itemVariantPicturePicturePredicate, itemVariantPictureItemVariantPredicate);

            Predicate itemVariantExistsPredicate = criteriaBuilder.exists(itemVariantPictureSubquery);
            predicates.add(itemVariantExistsPredicate);
        }

        Item item = pictureSearch.getItem();
        if (item != null) {
            Subquery<ItemPicture> itemPictureSubquery = query.subquery(ItemPicture.class);
            Root<ItemPicture> itemPictureRoot = itemPictureSubquery.from(ItemPicture.class);

            Path<Picture> itemPicturePicturePath = itemPictureRoot.get(ItemPicture_.picture);
            Predicate itemPicturePicturePredicate = criteriaBuilder.equal(itemPicturePicturePath, pictureRoot);

            Path<Item> itemPictureItemPath = itemPictureRoot.get(ItemPicture_.item);
            Predicate itemPictureItemPredicate = criteriaBuilder.equal(itemPictureItemPath, pictureRoot);

            itemPictureSubquery.where(itemPicturePicturePredicate, itemPictureItemPredicate);

            Predicate itemExistsPredicate = criteriaBuilder.exists(itemPictureSubquery);
            predicates.add(itemExistsPredicate);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Picture> typedQuery = entityManager.createQuery(query);
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AttributeDefinition> query = criteriaBuilder.createQuery(AttributeDefinition.class);
        Root<AttributeDefinition> attributeDefinitionRoot = query.from(AttributeDefinition.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = attributeSearch.getCompany();
        Path<Company> companyPath = attributeDefinitionRoot.get(AttributeDefinition_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        String multiSearch = attributeSearch.getMultiSearch();

        String nameContains = attributeSearch.getNameContains();
        if (nameContains != null) {
            Predicate nameContainsPredicate = createAttributeDefinitionNameContainsPredicate(attributeDefinitionRoot, nameContains);
            predicates.add(nameContainsPredicate);
        }

        String valueContains = attributeSearch.getValueContains();
        if (valueContains != null) {
            Predicate attributeHasValuePredicate = createAttributeHasValuePredicate(query, valueContains, attributeDefinitionRoot);
            predicates.add(attributeHasValuePredicate);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<AttributeDefinition> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private Predicate createAttributeHasValuePredicate(CriteriaQuery<AttributeDefinition> query, String valueContains, Root<AttributeDefinition> attributeDefinitionRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        Subquery<AttributeValue> attributeValueSubquery = query.subquery(AttributeValue.class);
        Root<AttributeValue> attributeValueRoot = attributeValueSubquery.from(AttributeValue.class);

        Predicate valueContainsPredicate = createAttributeValueContainsPredicate(attributeValueRoot, valueContains);

        Path<AttributeDefinition> attributeDefinitionPath = attributeValueRoot.get(AttributeValue_.attributeDefinition);
        Predicate attributeDefinitionPredicate = criteriaBuilder.equal(attributeDefinitionPath, attributeDefinitionRoot);

        attributeValueSubquery.where(valueContainsPredicate, attributeDefinitionPredicate);

        Predicate attributeValueExistsPredicate = criteriaBuilder.exists(attributeValueSubquery);
        return attributeValueExistsPredicate;
    }

}
