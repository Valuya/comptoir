package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.ItemSale_;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.Sale_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.search.SaleSearch;
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

        sale.setAccountingTransaction(accountingTransaction);

        for (ItemSale itemSale : itemSales) {
            Price price = itemSale.getPrice();
            BigDecimal vatExclusive = price.getVatExclusive();
            BigDecimal vatRate = price.getVatRate();
            BigDecimal vatAmount = VatUtils.calcVatAmount(price);
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

        sale.setVatExclusiveAmout(vatExclusiveTotal);
        sale.setVatAmount(vatTotal);

        return sale;
    }

    private AccountingEntry createSaleDebitAccountingEntry(Sale sale, AccountingEntry paymentAccountingEntry, ZonedDateTime dateTime) {
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = sale.getCompany();
        //TODO
//        AccountingEntry vatAccountingEntry = sale.getVatAccountingEntry();
        Account account = paymentAccountingEntry.getAccount();
        BigDecimal amount = paymentAccountingEntry.getAmount();

        AccountingEntry saleDebitAccountingEntry = new AccountingEntry();
        saleDebitAccountingEntry.setAccountingTransaction(accountingTransaction);
        saleDebitAccountingEntry.setCompany(company);
        saleDebitAccountingEntry.setDateTime(dateTime);
        saleDebitAccountingEntry.setAccount(account);
        saleDebitAccountingEntry.setAmount(amount);
//        saleDebitAccountingEntry.setVatAccountingEntry(vatAccountingEntry);

        return saleDebitAccountingEntry;
    }

    public Sale pay(Sale sale, List<ItemSale> itemSales, List<AccountingEntry> paymentAccountingEntryList, boolean close) {
        calcSale(sale, itemSales);

        ZonedDateTime dateTime = ZonedDateTime.now();

        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        for (AccountingEntry paymentAccountingEntry : paymentAccountingEntryList) {
            AccountingEntry saleDebitAccountingEntry = createSaleDebitAccountingEntry(sale, paymentAccountingEntry, dateTime);
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
    public BigDecimal calcPayBackAmount(@Nonnull Sale sale, @Nonnull List<ItemSale> itemSales, @Nonnull List<AccountingEntry> paymentAccountingEntryList) {
        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        for (AccountingEntry paymentAccountingEntry : paymentAccountingEntryList) {
            BigDecimal payedAmount = paymentAccountingEntry.getAmount();
            totalPayedAmount = totalPayedAmount.add(payedAmount);
        }

        Sale adjustedSale = calcSale(sale, itemSales);
        BigDecimal vatExclusiveAmount = adjustedSale.getVatExclusiveAmout();
        BigDecimal vatAmount = adjustedSale.getVatAmount();
        BigDecimal totalAmount = vatExclusiveAmount.add(vatAmount);
        BigDecimal payBackAmount = totalAmount.subtract(totalPayedAmount);
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

    public Sale findSaleById(Long saleId) {
        return entityManager.find(Sale.class, saleId);
    }

    @Nonnull
    public List<Sale> findSales(@Nonnull SaleSearch saleSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sale> query = criteriaBuilder.createQuery(Sale.class);
        Root<Sale> saleRoot = query.from(Sale.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = saleSearch.getCompany();
        Path<Company> companyPath = saleRoot.get(Sale_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Sale> typedQuery = entityManager.createQuery(query);

        List<Sale> sales = typedQuery.getResultList();

        return sales;
    }

    public Sale saveSale(Sale sale) {
        return entityManager.merge(sale);
    }
}
