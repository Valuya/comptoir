package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.commercial.ItemPrice;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.ItemSale_;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class SaleService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private StockService stockService;

    public Sale createSale(Stock stock, Sale sale, List<ItemSale> itemSales) {
        Sale managedSale = entityManager.merge(sale);

        for (ItemSale itemSale : itemSales) {
            ItemSale managedItemSale = entityManager.merge(itemSale);
            managedItemSale.setSale(managedSale);
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            stockService.adaptStockFromItemSale(zonedDateTime, stock, managedItemSale, StockChangeType.SALE, null);
        }

        // TODO: create accounting entry
        return managedSale;
    }

    @Nonnull
    public List<ItemSale> findSaleItems(@Nonnull Sale sale) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemSale> query = criteriaBuilder.createQuery(ItemSale.class);

        Root<ItemSale> itemSaleRoot = query.from(ItemSale.class);
        Path<Sale> salePath = itemSaleRoot.get(ItemSale_.sale);
        Predicate salePredicate = criteriaBuilder.equal(salePath, sale);

        query.where(salePredicate);

        TypedQuery<ItemSale> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public Sale calcSale(Sale sale, List<ItemSale> itemSales) {
        BigDecimal vatExclusiveTotal = BigDecimal.ZERO;
        BigDecimal vatTotal = BigDecimal.ZERO;
        for (ItemSale itemSale : itemSales) {
            ItemPrice price = itemSale.getPrice();
            BigDecimal vatExclusive = price.getVatExclusive();
            BigDecimal vatRate = price.getVatRate();
            BigDecimal vatAmount = vatExclusive.multiply(vatRate);
            vatExclusiveTotal = vatExclusiveTotal.add(vatExclusive);
            vatTotal = vatTotal.add(vatAmount);
        }

        Company company = sale.getCompany();
        ZonedDateTime dateTime = ZonedDateTime.now();

        AccountingEntry accountingEntry = new AccountingEntry();
        accountingEntry.setSale(sale);
        accountingEntry.setCompany(company);
        accountingEntry.setDateTime(dateTime);
        accountingEntry.setVatExclusiveAmount(vatExclusiveTotal);
        accountingEntry.setVatAmount(vatTotal);

        sale.setAccountingEntry(accountingEntry);

        return sale;
    }

    public Sale closeSale(Sale sale, List<ItemSale> itemSales, Account account) {
        calcSale(sale, itemSales);
        AccountingEntry accountingEntry = sale.getAccountingEntry();
        accountingEntry.setFromAccount(account);

        Sale managedSale = entityManager.merge(sale);
        return managedSale;
    }

    public BigDecimal calcPayBackAmount(Sale sale, List<ItemSale> itemSales, BigDecimal payedAmount) {
        Sale adjustedSale = calcSale(sale, itemSales);
        AccountingEntry accountingEntry = adjustedSale.getAccountingEntry();
        BigDecimal vatExclusiveAmount = accountingEntry.getVatExclusiveAmount();
        BigDecimal vatAmount = accountingEntry.getVatAmount();
        BigDecimal totalAmount = vatExclusiveAmount.add(vatAmount);
        BigDecimal payBackAmount = totalAmount.subtract(payedAmount);
        return payBackAmount;
    }
}
