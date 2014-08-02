package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.misc.LocaleText;
import be.valuya.comptoir.model.misc.LocaleText_;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.ItemStock_;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sorting;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
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

    public List<Item> findItems(ItemSearch itemSearch, Pagination<Item, ItemColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = criteriaBuilder.createQuery(Item.class);
        Root<Item> itemRoot = query.from(Item.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = itemSearch.getCompany();
        Path<Company> companyPath = itemRoot.get(Item_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        String barCode = itemSearch.getBarCode();
        if (barCode != null && !barCode.trim().isEmpty()) {
            barCode = barCode.trim();
            Path<String> barCodePath = itemRoot.get(Item_.barCode);
            Predicate barCodePredicate = criteriaBuilder.equal(barCodePath, barCode);
            predicates.add(barCodePredicate);
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

    public Sale createSale(Stock stock, Sale sale, List<ItemSale> itemSales) {
        Sale managedSale = entityManager.merge(sale);
        
        for (ItemSale itemSale : itemSales) {
            ItemSale managedItemSale = entityManager.merge(itemSale);
            managedItemSale.setSale(managedSale);
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            adaptStock(stock, managedItemSale, zonedDateTime);
        }
        
        // TODO: create accounting entry
        
        return managedSale;
    }

    private ItemStock adaptStock(Stock stock, ItemSale managedItemSale, ZonedDateTime zonedDateTime) {
        Item managedItem = managedItemSale.getItem();
        BigDecimal soldQuantity = managedItemSale.getQuantity();

        // find previous stock value
        ItemStock managedPreviousItemStock = findItemStock(managedItem, stock, zonedDateTime);

        // handle old values
        BigDecimal oldQuantity;
        if (managedPreviousItemStock == null) {
            oldQuantity = BigDecimal.ZERO;
        } else {
            oldQuantity = managedPreviousItemStock.getQuantity();
            managedPreviousItemStock.setEndDateTime(zonedDateTime);
        }

        // adapt quantities
        BigDecimal newQuantity = oldQuantity.subtract(soldQuantity);

        // create new stock value
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(stock);
        itemStock.setItem(managedItem);
        itemStock.setStartDateTime(zonedDateTime);
        itemStock.setPreviousItemStock(managedPreviousItemStock);
        itemStock.setQuantity(newQuantity);

        // persist
        ItemStock managedItemStock = entityManager.merge(itemStock);

        return managedItemStock;
    }

    public ItemStock findItemStock(Item item, Stock stock, ZonedDateTime atDateTime) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemStock> query = criteriaBuilder.createQuery(ItemStock.class);
        Root<ItemStock> itemStockRoot = query.from(ItemStock.class);

        List<Predicate> predicates = new ArrayList<>();

        Path<Stock> stockPath = itemStockRoot.get(ItemStock_.stock);
        Predicate stockPredicate = criteriaBuilder.equal(stockPath, stock);
        predicates.add(stockPredicate);

        Path<Item> itemPath = itemStockRoot.get(ItemStock_.item);
        Predicate itemPredicate = criteriaBuilder.equal(itemPath, item);
        predicates.add(itemPredicate);

        Path<ZonedDateTime> startDateTimePath = itemStockRoot.get(ItemStock_.startDateTime);
        Predicate startDateTimePredicate = criteriaBuilder.greaterThanOrEqualTo(startDateTimePath, atDateTime);
        predicates.add(startDateTimePredicate);

        Path<ZonedDateTime> toDateTimePath = itemStockRoot.get(ItemStock_.endDateTime);
        Predicate noEndPredicate = criteriaBuilder.isNull(toDateTimePath);
        Predicate endAfterPredicate = criteriaBuilder.lessThan(toDateTimePath, atDateTime);
        Predicate endDateTimePredicate = criteriaBuilder.or(noEndPredicate, endAfterPredicate);
        predicates.add(endDateTimePredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<ItemStock> typedQuery = entityManager.createQuery(query);
        try {
            ItemStock managedItemStock = typedQuery.getSingleResult();
            return managedItemStock;
        } catch (NoResultException noResultException) {
            return null;
        }
    }

}
