package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.misc.LocaleText;
import be.valuya.comptoir.model.misc.LocaleText_;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.ItemStock_;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sorting;
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

        String reference = itemSearch.getReference();
        if (reference != null && !reference.trim().isEmpty()) {
            reference = reference.trim();
            Path<String> referencePath = itemRoot.get(Item_.reference);
            Predicate referencePredicate = criteriaBuilder.equal(referencePath, reference);
            predicates.add(referencePredicate);
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
            Join<Item, LocaleText> nameJoin = itemRoot.join(Item_.name);
            MapJoin<LocaleText, Locale, String> localeTextMapJoin = nameJoin.join(LocaleText_.localeTextMap);
            Path<String> textPath = localeTextMapJoin.value();
            Expression<Integer> locationExpression = criteriaBuilder.locate(textPath, nameContains);
            Predicate namePredicate = criteriaBuilder.greaterThan(locationExpression, 0);
            predicates.add(namePredicate);
        }

        String descriptionContains = itemSearch.getDescriptionContains();
        if (descriptionContains != null && !descriptionContains.trim().isEmpty()) {
            Join<Item, LocaleText> descriptionJoin = itemRoot.join(Item_.description);
            MapJoin<LocaleText, Locale, String> localeTextMapJoin = descriptionJoin.join(LocaleText_.localeTextMap);
            Path<String> textPath = localeTextMapJoin.value();
            Expression<Integer> locationExpression = criteriaBuilder.locate(textPath, descriptionContains);
            Predicate descriptionPredicate = criteriaBuilder.greaterThan(locationExpression, 0);
            predicates.add(descriptionPredicate);
        }

        if (pagination != null) {
            List<Sorting<ItemColumn>> sortings = pagination.getSortings();
            List<Order> orders = ItemColumnPersistenceUtil.createOrdersFromSortings(criteriaBuilder, itemRoot, sortings);
            query.orderBy(orders);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

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

    public ItemStock adaptStockFromItemSale(ZonedDateTime fromDateTime, Stock stock, ItemSale managedItemSale) {
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

        return adaptStock(fromDateTime, stock, managedItem, newQuantity, managedPreviousItemStock);
    }

    public ItemStock adaptStock(ZonedDateTime fromDateTime, Stock stock, Item managedItem, BigDecimal newQuantity) {
        // find previous stock value
        ItemStock managedPreviousItemStock = findItemStock(managedItem, stock, fromDateTime);

        return adaptStock(fromDateTime, stock, managedItem, newQuantity, managedPreviousItemStock);
    }

    /**
     * Adapt stock values by creating a new ItemStock and updating the previous one.
     *
     * @param fromDateTime
     * @param stock
     * @param managedItem
     * @param newQuantity
     * @param managedPreviousItemStock
     * @return
     */
    public ItemStock adaptStock(ZonedDateTime fromDateTime, Stock stock, Item managedItem, BigDecimal newQuantity, ItemStock managedPreviousItemStock) {
        // create new stock value
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(stock);
        itemStock.setItem(managedItem);
        itemStock.setStartDateTime(fromDateTime);
        itemStock.setPreviousItemStock(managedPreviousItemStock);
        itemStock.setQuantity(newQuantity);

        if (managedPreviousItemStock != null) {
            managedPreviousItemStock.setEndDateTime(fromDateTime);
        }

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
        Join<ItemStock, Item> itemJoin = itemStockRoot.join(ItemStock_.item, JoinType.INNER);
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
            Predicate startDateTimePredicate = criteriaBuilder.greaterThanOrEqualTo(startDateTimePath, atDateTime);
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

}
