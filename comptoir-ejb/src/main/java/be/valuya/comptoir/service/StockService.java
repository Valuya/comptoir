package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.ItemPicture_;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.lang.LocaleText_;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.ItemStock_;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.stock.Stock_;
import be.valuya.comptoir.util.pagination.ItemColumn;
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
    public List<Item> findItems(@Nonnull ItemSearch itemSearch, @CheckForNull Pagination<Item, ItemColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = criteriaBuilder.createQuery(Item.class);
        Root<Item> itemRoot = query.from(Item.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = itemSearch.getCompany();
        Path<Company> companyPath = itemRoot.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Path<String> referencePath = itemRoot.get(Item_.reference);

        String multiSearch = itemSearch.getMultiSearch();
        if (multiSearch != null && !multiSearch.isEmpty()) {
            String lowerMultiSearch = multiSearch.toLowerCase(Locale.FRENCH); //TODO: actual locale

            Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemRoot, lowerMultiSearch);
            Predicate namePredicate = createNameContainsPredicate(itemRoot, lowerMultiSearch);
            Predicate descriptionPredicate = createDescriptionContainsPredicate(itemRoot, lowerMultiSearch);

            Predicate multiSearchPredicate = criteriaBuilder.or(referenceContainsPredicate, namePredicate, descriptionPredicate);
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
            Predicate referenceContainsPredicate = createReferenceContainsPredicate(itemRoot, referenceContains);
            predicates.add(referenceContainsPredicate);
        }

        String model = itemSearch.getModel();
        if (model != null && !model.trim().isEmpty()) {
            model = model.trim();
            Path<String> modelPath = itemRoot.get(Item_.model);
            Predicate modelPredicate = criteriaBuilder.equal(modelPath, model);
            predicates.add(modelPredicate);
        }

        String nameContains = itemSearch.getNameContains();
        if (nameContains != null && !nameContains.trim().isEmpty()) {
            nameContains = nameContains.trim().toLowerCase(Locale.FRENCH);
            Predicate namePredicate = createNameContainsPredicate(itemRoot, nameContains);
            predicates.add(namePredicate);
        }

        String descriptionContains = itemSearch.getDescriptionContains();
        if (descriptionContains != null && !descriptionContains.trim().isEmpty()) {
            descriptionContains = descriptionContains.trim().toLowerCase(Locale.FRENCH);
            Predicate descriptionPredicate = createDescriptionContainsPredicate(itemRoot, descriptionContains);
            predicates.add(descriptionPredicate);
        }

        if (pagination != null) {
            List<Sort<ItemColumn>> sortings = pagination.getSortings();
            List<Order> orders = ItemColumnPersistenceUtil.createOrdersFromSortings(criteriaBuilder, itemRoot, sortings);
            query.orderBy(orders);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);
        query.distinct(true);

        TypedQuery<Item> typedQuery = entityManager.createQuery(query);

        if (pagination != null) {
            int offset = pagination.getOffset();
            int maxResults = pagination.getMaxResults();
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(maxResults);
        }

        List<Item> items = typedQuery.getResultList();

        return items;
    }

    private Predicate createReferenceContainsPredicate(Root<Item> itemRoot, String referenceContains) {
        Path<String> referencePath = itemRoot.get(Item_.reference);

        return createContainsPredicate(referencePath, referenceContains);
    }

    private Predicate createContainsPredicate(Path<String> path, String text) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Expression<String> lowerReferenceExpression = criteriaBuilder.lower(path);
        Expression<Integer> locationExpression = criteriaBuilder.locate(lowerReferenceExpression, text);
        Predicate referenceContainsPredicate = criteriaBuilder.greaterThan(locationExpression, 0);
        return referenceContainsPredicate;
    }

    private Predicate createDescriptionContainsPredicate(Root<Item> itemRoot, String descriptionContains) {
        Join<Item, LocaleText> descriptionJoin = itemRoot.join(Item_.description);
        MapJoin<LocaleText, Locale, String> localeTextMapJoin = descriptionJoin.join(LocaleText_.localeTextMap);
        Path<String> textPath = localeTextMapJoin.value();
        Predicate descriptionPredicate = createContainsPredicate(textPath, descriptionContains);
        return descriptionPredicate;
    }

    private Predicate createNameContainsPredicate(Root<Item> itemRoot, String nameContains) {
        Join<Item, LocaleText> nameJoin = itemRoot.join(Item_.name);
        MapJoin<LocaleText, Locale, String> localeTextMapJoin = nameJoin.join(LocaleText_.localeTextMap);
        Path<String> textPath = localeTextMapJoin.value();
        Predicate namePredicate = createContainsPredicate(textPath, nameContains);
        return namePredicate;
    }

    public ItemStock adaptStockFromItemSale(ZonedDateTime fromDateTime, Stock stock, ItemSale managedItemSale, StockChangeType stockChangeType, String comment) {
        Item managedItem = managedItemSale.getItem();
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
    public ItemStock adaptStock(@Nonnull ZonedDateTime fromDateTime, @Nonnull Stock stock, @Nonnull Item managedItem, @Nonnull BigDecimal newQuantity, @CheckForNull String comment) {
        // create new stock value
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(stock);
        itemStock.setItem(managedItem);
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
        Join<ItemStock, Item> itemJoin = itemStockRoot.join(ItemStock_.item, JoinType.LEFT);
        Path<Company> companyPath = itemJoin.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Stock stock = itemStockSearch.getStock();
        if (stock != null) {
            Path<Stock> stockPath = itemStockRoot.get(ItemStock_.stock);
            Predicate stockPredicate = criteriaBuilder.equal(stockPath, stock);
            predicates.add(stockPredicate);
        }

        Item item = itemStockSearch.getItem();
        if (item != null) {
            Predicate itemPredicate = criteriaBuilder.equal(itemJoin, item);
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
    public List<ItemStock> findItemStocks(@Nonnull Item item, @Nonnull ZonedDateTime atDateTime) {
        Company company = item.getCompany();
        ItemStockSearch itemStockSearch = new ItemStockSearch();
        itemStockSearch.setCompany(company);
        itemStockSearch.setItem(item);
        itemStockSearch.setAtDateTime(atDateTime);

        return findItemStocks(itemStockSearch);
    }

    @Nonnull
    public List<ItemStock> findItemStocks(Item item) {
        ZonedDateTime dateTime = ZonedDateTime.now();
        return findItemStocks(item, dateTime);
    }

    /**
     * Find ItemStock at given time for item, in given Stock.
     *
     * @param item
     * @param stock
     * @param atDateTime
     * @return
     */
    @CheckForNull
    public ItemStock findItemStock(@Nonnull Item item, @Nonnull Stock stock, @Nonnull ZonedDateTime atDateTime) {
        Company company = item.getCompany();
        ItemStockSearch itemStockSearch = new ItemStockSearch();
        itemStockSearch.setCompany(company);
        itemStockSearch.setStock(stock);
        itemStockSearch.setItem(item);
        itemStockSearch.setAtDateTime(atDateTime);

        List<ItemStock> itemStocks = findItemStocks(itemStockSearch);
        if (itemStocks.isEmpty()) {
            return null;
        }
        if (itemStocks.size() > 1) {
            String errorMessage = MessageFormat.format("Multiple stock entries for item {0} in stock {1} at {2}", item, stock, atDateTime);
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

    public Item saveItem(Item item, Stock stock, BigDecimal initialQuantity) {
        Item managedItem = entityManager.merge(item);
        ZonedDateTime dateTime = ZonedDateTime.now();

        adaptStock(dateTime, stock, managedItem, initialQuantity, null);

        return managedItem;
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
    public Item saveItem(Item item) {
        return entityManager.merge(item);
    }

    public ItemPicture saveItemPicture(ItemPicture itemPicture) {
        Item item = itemPicture.getItem();
        ItemPicture managedPicture = entityManager.merge(itemPicture);
        item.setMainPicture(itemPicture);
        Item managedItem = entityManager.merge(item);
        return managedPicture;
    }

    public List<ItemPicture> findItemPictures(@Nonnull Item item) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemPicture> query = criteriaBuilder.createQuery(ItemPicture.class);
        Root<ItemPicture> itemPictureRoot = query.from(ItemPicture.class);

        List<Predicate> predicates = new ArrayList<>();

        Path<Item> itemPath = itemPictureRoot.get(ItemPicture_.item);
        Predicate itemPredicate = criteriaBuilder.equal(itemPath, item);
        predicates.add(itemPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<ItemPicture> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

}
