package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.commercial.*;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.lang.LocaleText_;
import be.valuya.comptoir.model.search.*;
import be.valuya.comptoir.model.stock.*;
import be.valuya.comptoir.util.pagination.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        query.distinct(true);

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
        Predicate activePredicate = criteriaBuilder.isTrue(activePath);
        predicates.add(activePredicate);

        Company company = itemSearch.getCompany();
        Path<Company> companyPath = itemFrom.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        String multiSearch = itemSearch.getMultiSearch();
        Locale locale = itemSearch.getLocale();
        if (multiSearch != null && !multiSearch.trim().isEmpty()) {
            Predicate multiSearchPredicate = createItemMultiSearchPredicate(multiSearch, itemFrom, itemVariantFrom, criteriaBuilder, locale);
            predicates.add(multiSearchPredicate);
        }

        Boolean multipleSale = itemSearch.getMultipleSale();
        if (multipleSale != null) {
            Path<Boolean> multipleSalePath = itemFrom.get(Item_.multipleSale);
            Predicate multipleSalePredicate = criteriaBuilder.equal(multipleSalePath, multipleSale);
            predicates.add(multipleSalePredicate);
        }

        String reference = itemSearch.getReference();
        if (reference != null && !reference.trim().isEmpty()) {
            Path<String> referencePath = itemFrom.get(Item_.reference);
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
            itemFrom.fetch(Item_.name, JoinType.LEFT);
            Join<Item, LocaleText> nameJoin = itemFrom.join(Item_.name, JoinType.LEFT);
            Predicate namePredicate = createLocaleTextContainsPredicate(nameJoin, nameContains, locale, predicates, criteriaBuilder);
            predicates.add(namePredicate);
        }

        String descriptionContains = itemSearch.getDescriptionContains();
        if (descriptionContains != null && !descriptionContains.trim().isEmpty()) {
            descriptionContains = descriptionContains.trim().toLowerCase(Locale.FRENCH);
            itemFrom.fetch(Item_.description, JoinType.LEFT);
            Join<Item, LocaleText> descriptionJoin = itemFrom.join(Item_.description, JoinType.LEFT);
            Predicate descriptionPredicate = createLocaleTextContainsPredicate(descriptionJoin, descriptionContains, locale, predicates, criteriaBuilder);
            predicates.add(descriptionPredicate);
        }

        return predicates;
    }

    private Predicate createItemMultiSearchPredicate(String multiSearch, From<?, Item> itemFrom, From<?, ItemVariant> itemVariantFrom, CriteriaBuilder criteriaBuilder, Locale locale) {
        String lowerMultiSearch;
        if (locale == null) {
            lowerMultiSearch = multiSearch.toLowerCase();
        } else {
            lowerMultiSearch = multiSearch.toLowerCase(locale);
        }
        List<Predicate> multiSearchPredicates = new ArrayList<>();
        List<Predicate> localeTextRestrictions = new ArrayList<>();

        Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemFrom, lowerMultiSearch);
        multiSearchPredicates.add(referenceContainsPredicate);

        Join<Item, LocaleText> nameJoin = itemFrom.join(Item_.name, JoinType.LEFT);
        Predicate namePredicate = createLocaleTextContainsPredicate(nameJoin, lowerMultiSearch, locale, localeTextRestrictions, criteriaBuilder);
        multiSearchPredicates.add(namePredicate);

        Join<Item, LocaleText> descriptionJoin = itemFrom.join(Item_.description, JoinType.LEFT);
        Predicate descriptionPredicate = createLocaleTextContainsPredicate(descriptionJoin, lowerMultiSearch, locale, localeTextRestrictions, criteriaBuilder);
        multiSearchPredicates.add(descriptionPredicate);

        if (itemVariantFrom != null) {
            Predicate variantReferenceContainsPredicate = createVariantReferenceContainsPredicate(itemVariantFrom, lowerMultiSearch);
            multiSearchPredicates.add(variantReferenceContainsPredicate);

            ListJoin<ItemVariant, AttributeValue> attributesValues = itemVariantFrom.join(ItemVariant_.attributeValues, JoinType.LEFT);
            Join<AttributeValue, LocaleText> valueTextsJoin = attributesValues.join(AttributeValue_.value, JoinType.LEFT);
            Predicate valueContainsPredicate = createLocaleTextContainsPredicate(valueTextsJoin, multiSearch, locale, localeTextRestrictions, criteriaBuilder);
            multiSearchPredicates.add(valueContainsPredicate);

            Join<AttributeValue, AttributeDefinition> definitionJoin = attributesValues.join(AttributeValue_.attributeDefinition, JoinType.LEFT);
            Join<AttributeDefinition, LocaleText> defintionNameJoin = definitionJoin.join(AttributeDefinition_.name, JoinType.LEFT);
            Predicate definitionContainsPredicate = createLocaleTextContainsPredicate(defintionNameJoin, multiSearch, locale, localeTextRestrictions, criteriaBuilder);
            multiSearchPredicates.add(definitionContainsPredicate);
        }
        // combine
        Predicate[] multiSearchPredicateArray = multiSearchPredicates.toArray(new Predicate[0]);
        Predicate multiSearchPredicate = criteriaBuilder.or(multiSearchPredicateArray);

        Predicate[] localeTextRestrictionsArray = localeTextRestrictions.toArray(new Predicate[0]);

        Predicate localeTextRestrictionsPredicate = criteriaBuilder.and(localeTextRestrictionsArray);
        return criteriaBuilder.and(multiSearchPredicate, localeTextRestrictionsPredicate);
    }

    private Predicate createVariantReferenceContainsPredicate(Path<ItemVariant> itemVariantPath, String referenceContains) {
        Path<String> referencePath = itemVariantPath.get(ItemVariant_.variantReference);

        return createContainsPredicate(referencePath, referenceContains);
    }

    private Predicate createReferenceContainsPredicate(Path<Item> itemPath, String referenceContains) {
        Path<String> referencePath = itemPath.get(Item_.reference);

        return createContainsPredicate(referencePath, referenceContains);
    }

    private Predicate createLocalePredicate(Path<Locale> localePath, @Nonnull Locale locale) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        String language = locale.getLanguage(); // TODO: file an EL bug to remove this workaround
        Predicate predicate = criteriaBuilder.equal(localePath, language);
        return predicate;
    }

    private Predicate createContainsPredicate(Path<String> path, String text) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Expression<String> lowerReferenceExpression = criteriaBuilder.lower(path);
        Expression<Integer> locationExpression = criteriaBuilder.locate(lowerReferenceExpression, text);
        Predicate referenceContainsPredicate = criteriaBuilder.greaterThan(locationExpression, 0);
        return referenceContainsPredicate;
    }

    private Predicate createLocaleTextContainsPredicate(From<?, LocaleText> localeTextPath, String contains, @CheckForNull Locale locale, List<Predicate> localeTextrestrictions, CriteriaBuilder builder) {
        MapJoin<LocaleText, Locale, String> localeTextMapJoin = localeTextPath.join(LocaleText_.localeTextMap, JoinType.LEFT);
        Path<String> textPath = localeTextMapJoin.value();

        if (locale != null) {
            Predicate noLocaleTextPredicate = builder.isNull(localeTextMapJoin);
            Path<Locale> localePath = localeTextMapJoin.key();
            Predicate localePredicate = createLocalePredicate(localePath, locale);
            Predicate finalLocalePredicate = builder.or(noLocaleTextPredicate, localePredicate);
            localeTextrestrictions.add(finalLocalePredicate);
        }

        Predicate namePredicate = createContainsPredicate(textPath, contains);
        return namePredicate;
    }

    public ItemStock adaptStockFromItemSale(ZonedDateTime fromDateTime,ItemVariantSale managedItemSale, StockChangeType stockChangeType, String comment) {
        ItemVariant managedItem = managedItemSale.getItemVariant();
        BigDecimal soldQuantity = managedItemSale.getQuantity();
        Stock stock = managedItemSale.getStock();

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
        ItemStock adaptedStock = adaptStock(fromDateTime, stock, managedItem, newQuantity, comment, stockChangeType);

        // Reference sale
        Sale sale = managedItemSale.getSale();
        adaptedStock.setStockChangeSale(sale);
        return saveItemStock(adaptedStock);
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
    public ItemStock adaptStock(@Nonnull ZonedDateTime fromDateTime, @Nonnull Stock stock, @Nonnull ItemVariant managedItem, @Nonnull BigDecimal newQuantity, @CheckForNull String comment, @Nonnull StockChangeType stockChangeType) {
        // find previous stock
        ItemStock managedPreviousItemStock = findItemStock(managedItem, stock, fromDateTime);
        if (managedPreviousItemStock != null) {
            managedPreviousItemStock.setEndDateTime(fromDateTime);
            entityManager.merge(managedPreviousItemStock);
        }

        // create new stock value
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(stock);
        itemStock.setItemVariant(managedItem);
        itemStock.setStartDateTime(fromDateTime);
        itemStock.setQuantity(newQuantity);
        itemStock.setComment(comment);
        itemStock.setStockChangeType(stockChangeType);
        itemStock.setPreviousItemStock(managedPreviousItemStock);

        // persist
        ItemStock managedItemStock = entityManager.merge(itemStock);

        return managedItemStock;
    }

    @Nonnull
    public List<ItemStock> findItemStocks(@Nonnull ItemStockSearch itemStockSearch) {
        return findItemStocks(itemStockSearch, null);
    }

    @Nonnull
    public List<ItemStock> findItemStocks(@Nonnull ItemStockSearch itemStockSearch, Pagination<ItemStock, ItemVariantStockColumn> pagination) {
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
            Predicate itemPredicate = criteriaBuilder.equal(itemVariantJoin, itemVariant);
            predicates.add(itemPredicate);
        }

        ZonedDateTime atDateTime = itemStockSearch.getAtDateTime();
        if (atDateTime != null) {
            Path<ZonedDateTime> startDateTimePath = itemStockRoot.get(ItemStock_.startDateTime);
            Predicate startDateTimePredicate = criteriaBuilder.lessThanOrEqualTo(startDateTimePath, atDateTime);
            predicates.add(startDateTimePredicate);

            Path<ZonedDateTime> toDateTimePath = itemStockRoot.get(ItemStock_.endDateTime);
            Predicate noEndPredicate = criteriaBuilder.isNull(toDateTimePath);
            Predicate endAfterPredicate = criteriaBuilder.greaterThan(toDateTimePath, atDateTime);
            Predicate endDateTimePredicate = criteriaBuilder.or(noEndPredicate, endAfterPredicate);
            predicates.add(endDateTimePredicate);
        }


        paginatedQueryService.applySort(pagination, itemStockRoot, query,
                stockColumn -> ItemVariantStockColumnPersistenceUtil.getPath(itemStockRoot, stockColumn)
        );
        List<ItemStock> itemStockList = paginatedQueryService.getResults(predicates, query, itemStockRoot, pagination);
        return itemStockList;
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

    @CheckForNull
    public ItemStock findItemStockById(@Nonnull Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemStock> query = criteriaBuilder.createQuery(ItemStock.class);
        Root<ItemStock> itemStockRoot = query.from(ItemStock.class);

        Path<Long> idPath = itemStockRoot.get(ItemStock_.id);
        Predicate idPredicate = criteriaBuilder.equal(idPath, id);

        query.where(idPredicate);

        TypedQuery<ItemStock> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
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
    public Stock findStockById(@Nonnull Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = criteriaBuilder.createQuery(Stock.class);
        Root<Stock> stockRoot = query.from(Stock.class);

        Path<Long> idPath = stockRoot.get(Stock_.id);
        Predicate idPredicate = criteriaBuilder.equal(idPath, id);

        query.where(idPredicate);

        TypedQuery<Stock> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }

    @Nonnull
    public List<Stock> findStocks(@Nonnull StockSearch stockSearch, @CheckForNull Pagination<Stock, StockColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = criteriaBuilder.createQuery(Stock.class);
        Root<Stock> stockRoot = query.from(Stock.class);

        List<Predicate> predicateList = applyStockPredicates(stockSearch, stockRoot, criteriaBuilder, query);

        paginatedQueryService.applySort(pagination, stockRoot, query,
                stockColumn -> StockColumnPersistenceUtil.getPath(stockRoot, stockColumn)
        );

        List<Stock> stocks = paginatedQueryService.getResults(predicateList, query, stockRoot, pagination);
        return stocks;
    }

    private List<Predicate> applyStockPredicates(StockSearch stockSearch, Root<Stock> stockRoot, CriteriaBuilder criteriaBuilder, CriteriaQuery query) {
        List<Predicate> predicateList = new ArrayList<>();

        Company company = stockSearch.getCompany();
        Path<Company> companyPath = stockRoot.get(Stock_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicateList.add(companyPredicate);

        Boolean active = stockSearch.getActive();
        if (active != null) {
            Path<Boolean> activePath = stockRoot.get(Stock_.active);
            Predicate activePredicate = criteriaBuilder.equal(activePath, active);
            predicateList.add(activePredicate);
        }

        return predicateList;
    }

    public Stock saveStock(Stock stock) {
        Stock managedStock = entityManager.merge(stock);
        return managedStock;
    }
    private ItemStock saveItemStock(ItemStock itemStock) {
        ItemStock managedStock = entityManager.merge(itemStock);
        return managedStock;
    }

    public ItemVariant saveItem(ItemVariant item, Stock stock, BigDecimal initialQuantity) {
        ItemVariant managedItem = entityManager.merge(item);
        ZonedDateTime dateTime = ZonedDateTime.now();

        adaptStock(dateTime, stock, managedItem, initialQuantity, null,StockChangeType.INITIAL);

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
        boolean newItem = item.getId() == null;
        Item savedItem = entityManager.merge(item);
        if (!newItem) {
            return savedItem;
        }
        // 
        createDefaultVariant(savedItem);
        return savedItem;
    }

    private ItemVariant createDefaultVariant(Item item) {
        ItemVariant itemVariant = new ItemVariant();
        itemVariant.setActive(true);
        itemVariant.setItem(item);
        itemVariant.setPricing(Pricing.PARENT_ITEM);
        itemVariant.setVariantReference(item.getReference());
        return saveItemVariant(itemVariant);
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

        Locale locale = attributeSearch.getLocale();

        String nameContains = attributeSearch.getNameContains();
        if (nameContains != null) {
            Join<AttributeDefinition, LocaleText> attributeDefinitionNameJoin = attributeDefinitionRoot.join(AttributeDefinition_.name);
            Predicate nameContainsPredicate = createLocaleTextContainsPredicate(attributeDefinitionNameJoin, nameContains, locale, predicates, criteriaBuilder);
            predicates.add(nameContainsPredicate);
        }

        String valueContains = attributeSearch.getValueContains();
        if (valueContains != null) {
            Predicate attributeHasValuePredicate = createAttributeHasValuePredicate(query, valueContains, attributeDefinitionRoot, locale);
            predicates.add(attributeHasValuePredicate);
        }

        String multiSearch = attributeSearch.getMultiSearch();
        if (multiSearch != null) {
            Join<AttributeDefinition, LocaleText> attributeDefinitionNameJoin = attributeDefinitionRoot.join(AttributeDefinition_.name);
            Predicate nameContainsPredicate = createLocaleTextContainsPredicate(attributeDefinitionNameJoin, nameContains, locale, predicates, criteriaBuilder);
            Predicate hasValuePredicate = createAttributeHasValuePredicate(query, multiSearch, attributeDefinitionRoot, locale);
            Predicate multiSearchPredicate = criteriaBuilder.or(nameContainsPredicate, hasValuePredicate);
            predicates.add(multiSearchPredicate);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<AttributeDefinition> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private Predicate createAttributeHasValuePredicate(CriteriaQuery<AttributeDefinition> query, String valueContains, Root<AttributeDefinition> attributeDefinitionRoot, @CheckForNull Locale locale) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        Subquery<AttributeValue> attributeValueSubquery = query.subquery(AttributeValue.class);
        Root<AttributeValue> attributeValueRoot = attributeValueSubquery.from(AttributeValue.class);

        List<Predicate> predicates = new ArrayList<>();
        Join<AttributeValue, LocaleText> attributeValueJoin = attributeValueRoot.join(AttributeValue_.value);
        Predicate valueContainsPredicate = createLocaleTextContainsPredicate(attributeValueJoin, valueContains, locale, predicates, criteriaBuilder);

        Path<AttributeDefinition> attributeDefinitionPath = attributeValueRoot.get(AttributeValue_.attributeDefinition);
        Predicate attributeDefinitionPredicate = criteriaBuilder.equal(attributeDefinitionPath, attributeDefinitionRoot);

        predicates.add(valueContainsPredicate);
        predicates.add(attributeDefinitionPredicate);
        Predicate[] predicatesArray = predicates.toArray(new Predicate[0]);
        Predicate attributeHasValuePredicate = criteriaBuilder.and(predicatesArray);
        attributeValueSubquery.where(attributeHasValuePredicate);

        Predicate attributeValueExistsPredicate = criteriaBuilder.exists(attributeValueSubquery);
        return attributeValueExistsPredicate;
    }

}
