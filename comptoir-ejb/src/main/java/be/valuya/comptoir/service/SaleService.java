package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPrice;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.ItemSale_;
import be.valuya.comptoir.model.commercial.Payment;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.misc.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.thirdparty.Customer;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
        Company company = sale.getCompany();
        ZonedDateTime dateTime = ZonedDateTime.now();

        BigDecimal vatExclusiveTotal = BigDecimal.ZERO;
        BigDecimal vatTotal = BigDecimal.ZERO;

        Customer customer = sale.getCustomer();

        AccountingTransaction accountingTransaction = new AccountingTransaction();
        accountingTransaction.setCompany(company);
        accountingTransaction.setAccountingTransactionType(AccountingTransactionType.SALE);
        accountingTransaction.setDateTime(dateTime);
        accountingTransaction.setSale(sale);

        sale.setAccountingTransaction(accountingTransaction);

        for (ItemSale itemSale : itemSales) {
            ItemPrice price = itemSale.getPrice();
            BigDecimal vatExclusive = price.getVatExclusive();
            BigDecimal vatRate = price.getVatRate();
            BigDecimal vatAmount = vatExclusive.multiply(vatRate);
            vatExclusiveTotal = vatExclusiveTotal.add(vatExclusive);
            vatTotal = vatTotal.add(vatAmount);
            BigDecimal productCredit = vatExclusive.negate();

            Item item = itemSale.getItem();
            LocaleText description = item.getDescription();

            AccountingEntry productAccountingEntry = new AccountingEntry();
            productAccountingEntry.setCompany(company);
            productAccountingEntry.setCustomer(customer);
            productAccountingEntry.setDateTime(dateTime);
            productAccountingEntry.setAmount(productCredit);
            productAccountingEntry.setVatRate(vatRate);
            productAccountingEntry.setDescription(description);
            productAccountingEntry.setAccountingTransaction(accountingTransaction);
        }

        Account vatAccount = findAccountByType(company, AccountType.VAT);
        BigDecimal vatCredit = vatTotal.negate();

        AccountingEntry vatAccountingEntry = new AccountingEntry();
        vatAccountingEntry.setAccountingTransaction(accountingTransaction);
        vatAccountingEntry.setCompany(company);
        vatAccountingEntry.setDateTime(dateTime);
        vatAccountingEntry.setAmount(vatCredit);
        vatAccountingEntry.setAccount(vatAccount);

        return sale;
    }

    private AccountingEntry createSaleDebitAccountingEntry(Sale sale, Payment payment, ZonedDateTime dateTime) {
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = sale.getCompany();
        AccountingEntry vatAccountingEntry = sale.getVatAccountingEntry();
        Account account = payment.getAccount();
        BigDecimal amount = payment.getAmount();

        AccountingEntry saleDebitAccountingEntry = new AccountingEntry();
        saleDebitAccountingEntry.setAccountingTransaction(accountingTransaction);
        saleDebitAccountingEntry.setCompany(company);
        saleDebitAccountingEntry.setDateTime(dateTime);
        saleDebitAccountingEntry.setAccount(account);
        saleDebitAccountingEntry.setAmount(amount);
        saleDebitAccountingEntry.setVatAccountingEntry(vatAccountingEntry);

        return saleDebitAccountingEntry;
    }

    public Sale pay(Sale sale, List<ItemSale> itemSales, List<Payment> payments, boolean close) {
        calcSale(sale, itemSales);

        ZonedDateTime dateTime = ZonedDateTime.now();

        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        for (Payment payment : payments) {
            AccountingEntry saleDebitAccountingEntry = createSaleDebitAccountingEntry(sale, payment, dateTime);
            AccountingEntry managedSaleDebitAccountingEntry = entityManager.merge(saleDebitAccountingEntry);
            BigDecimal payedAmount = managedSaleDebitAccountingEntry.getAmount();
            totalPayedAmount = totalPayedAmount.add(payedAmount);
        }

        if (close) {
            BigDecimal vatAmount = sale.getVatAmount();
            BigDecimal vatExclusiveAmout = sale.getVatExclusiveAmout();
            BigDecimal vatInclusiveAmount = vatAmount.add(vatExclusiveAmout);

            if (vatInclusiveAmount.compareTo(totalPayedAmount) == 0) {
                sale.setClosed(true);
            }
        }

        Sale managedSale = entityManager.merge(sale);
        return managedSale;
    }

    @Nonnull
    public BigDecimal calcPayBackAmount(@Nonnull Sale sale, @Nonnull List<ItemSale> itemSales, @Nonnull BigDecimal payedAmount) {
        Sale adjustedSale = calcSale(sale, itemSales);
        BigDecimal vatExclusiveAmount = adjustedSale.getVatExclusiveAmout();
        BigDecimal vatAmount = adjustedSale.getVatAmount();
        BigDecimal totalAmount = vatExclusiveAmount.add(vatAmount);
        BigDecimal payBackAmount = totalAmount.subtract(payedAmount);
        return payBackAmount;
    }

    @Nonnull
    private Account findAccountByType(@Nonnull Company company, @Nonnull AccountType accountType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = query.from(Account.class);

        List<Predicate> predicates = new ArrayList<>();

        Path<Company> companyPath = accountRoot.get(Account_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Path<AccountType> accountTypePath = accountRoot.get(Account_.accountType);
        Predicate accountTypePredicate = criteriaBuilder.equal(accountTypePath, accountType);
        predicates.add(accountTypePredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Account> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }
}
