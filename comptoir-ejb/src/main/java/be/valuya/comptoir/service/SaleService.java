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
import be.valuya.comptoir.model.commercial.SalePrice;
import be.valuya.comptoir.model.commercial.Sale_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.model.search.ItemSaleSearch;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;
import be.valuya.comptoir.util.pagination.Sort;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
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
    @EJB
    private AccountService accountService;

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
    public List<ItemSale> findItemSales(@Nonnull Sale sale) {
        if (sale.getId() == null) {
            // new sale, no ItemSale list
            return Arrays.asList();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemSale> query = criteriaBuilder.createQuery(ItemSale.class);

        Root<ItemSale> itemSaleRoot = query.from(ItemSale.class);
        Path<Sale> salePath = itemSaleRoot.get(ItemSale_.sale);
        Predicate salePredicate = criteriaBuilder.equal(salePath, sale);

        query.where(salePredicate);

        TypedQuery<ItemSale> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public Sale calcSale(Sale sale) {
        List<ItemSale> itemSales = findItemSales(sale);

        return AccountingUtils.calcSale(sale, itemSales);
    }

    public void createSaleAccountingEntries(Sale sale, List<ItemSale> itemSales) {
        List<AccountingEntry> productAccountingEntries = itemSales.stream()
                .map(this::createItemSaleAccountingEntry)
                .collect(Collectors.toList());

        productAccountingEntries.forEach(entityManager::merge);

        AccountingEntry vatAccountingEntry = createSaleVatAccountingEntry(sale);
        entityManager.merge(vatAccountingEntry);
    }

    private AccountingEntry createSaleVatAccountingEntry(Sale sale) {
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = sale.getCompany();

        Account vatAccount = findAccountByType(company, AccountType.VAT);
        BigDecimal vatAmount = sale.getVatAmount();
        BigDecimal vatCredit = vatAmount.negate();

        ZonedDateTime dateTime = ZonedDateTime.now();

        Customer customer = sale.getCustomer();

        AccountingEntry vatAccountingEntry = new AccountingEntry();
        vatAccountingEntry.setAccountingTransaction(accountingTransaction);
        vatAccountingEntry.setCompany(company);
        vatAccountingEntry.setDateTime(dateTime);
        vatAccountingEntry.setAmount(vatCredit);
        vatAccountingEntry.setAccount(vatAccount);
        vatAccountingEntry.setCustomer(customer);

        return vatAccountingEntry;
    }

    private AccountingEntry createItemSaleAccountingEntry(ItemSale itemSale) {
        Sale sale = itemSale.getSale();
        Company company = sale.getCompany();
        ZonedDateTime dateTime = ZonedDateTime.now();

        Customer customer = sale.getCustomer();
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();

        Price price = itemSale.getPrice();
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal vatRate = price.getVatRate();
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

        return productAccountingEntry;
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
        Sale adjustedSale = AccountingUtils.calcSale(sale, itemSales);

        ZonedDateTime dateTime = ZonedDateTime.now();

        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        for (AccountingEntry paymentAccountingEntry : paymentAccountingEntryList) {
            AccountingEntry saleDebitAccountingEntry = createSaleDebitAccountingEntry(adjustedSale, paymentAccountingEntry, dateTime);
            AccountingEntry managedSaleDebitAccountingEntry = entityManager.merge(saleDebitAccountingEntry);
            BigDecimal payedAmount = managedSaleDebitAccountingEntry.getAmount();
            totalPayedAmount = totalPayedAmount.add(payedAmount);
        }

        if (close) {
            BigDecimal vatAmount = adjustedSale.getVatAmount();
            BigDecimal vatExclusiveAmout = adjustedSale.getVatExclusiveAmount();
            BigDecimal vatInclusiveAmount = vatAmount.add(vatExclusiveAmout);

            if (vatInclusiveAmount.compareTo(totalPayedAmount) == 0) {
                adjustedSale.setClosed(true);
            }
        }

        Sale managedSale = entityManager.merge(adjustedSale);
        return managedSale;
    }

    @Nonnull
    public BigDecimal calcPayBackAmount(@Nonnull Sale sale, @Nonnull List<ItemSale> itemSales, @Nonnull List<AccountingEntry> paymentAccountingEntryList) {
        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        for (AccountingEntry paymentAccountingEntry : paymentAccountingEntryList) {
            BigDecimal payedAmount = paymentAccountingEntry.getAmount();
            totalPayedAmount = totalPayedAmount.add(payedAmount);
        }

        Sale adjustedSale = AccountingUtils.calcSale(sale, itemSales);
        BigDecimal vatExclusiveAmount = adjustedSale.getVatExclusiveAmount();
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

    public ItemSale findItemSaleById(Long saleId) {
        return entityManager.find(ItemSale.class, saleId);
    }

    @Nonnull
    public List<Sale> findSales(@Nonnull SaleSearch saleSearch, @CheckForNull Pagination<Sale, SaleColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sale> query = criteriaBuilder.createQuery(Sale.class);
        Root<Sale> saleRoot = query.from(Sale.class);

        query.select(saleRoot);

        List<Predicate> predicates = applySaleSearch(saleSearch, saleRoot, criteriaBuilder);
        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        if (pagination != null) {
            List<Sort<SaleColumn>> sortings = pagination.getSortings();
            List<Order> orders = SaleColumnPersistenceUtils.createOrdersFromSortings(criteriaBuilder, saleRoot, sortings);
            query.orderBy(orders);
        }

        TypedQuery<Sale> typedQuery = entityManager.createQuery(query);

        if (pagination != null) {
            int offset = pagination.getOffset();
            int maxResults = pagination.getMaxResults();
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(maxResults);
        }

        List<Sale> sales = typedQuery.getResultList();
        return sales;
    }

    @Nonnull
    public Long countSales(@Nonnull SaleSearch saleSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Sale> saleRoot = query.from(Sale.class);

        Expression<Long> saleCountExpression = criteriaBuilder.count(saleRoot);
        query.select(saleCountExpression);

        List<Predicate> predicates = applySaleSearch(saleSearch, saleRoot, criteriaBuilder);
        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        Long saleCount = typedQuery.getSingleResult();
        return saleCount;
    }

    private List<Predicate> applySaleSearch(SaleSearch saleSearch, Path<Sale> saleRoot, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Company company = saleSearch.getCompany();
        Path<Company> companyPath = saleRoot.get(Sale_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Boolean closed = saleSearch.getClosed();
        if (closed != null) {
            Path<Boolean> closedPath = saleRoot.get(Sale_.closed);
            Predicate closedPredicate = criteriaBuilder.equal(closedPath, closed);
            predicates.add(closedPredicate);
        }
        return predicates;
    }

    @SuppressWarnings("null")
    public Sale saveSale(Sale sale) {
        Company company = sale.getCompany();

        ZonedDateTime dateTime = sale.getDateTime();
        if (dateTime == null) {
            dateTime = ZonedDateTime.now();
            sale.setDateTime(dateTime);
        }

        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        if (accountingTransaction == null) {
            // not accounting transaction, create one
            accountingTransaction = new AccountingTransaction();
            accountingTransaction.setCompany(company);
            accountingTransaction.setDateTime(dateTime);
            accountingTransaction.setAccountingTransactionType(AccountingTransactionType.SALE);

            sale.setAccountingTransaction(accountingTransaction);
        }

        calcSale(sale);

        Sale managedSale = entityManager.merge(sale);
        managedSale = calcSale(managedSale);

        return managedSale;
    }

    public void cancelOpenSale(Sale sale) {
        if (sale.isClosed()) {
            throw new IllegalArgumentException("The sale is closed");
        }
        List<ItemSale> itemSaleList = findItemSales(sale);
        for (ItemSale itemSale : itemSaleList) {
            removeItemSale(itemSale);
        }
        Sale managedSale = entityManager.merge(sale);
        entityManager.remove(managedSale);
    }

    public ItemSale saveItemSale(ItemSale itemSale) {
        ZonedDateTime dateTime = itemSale.getDateTime();
        if (dateTime == null) {
            dateTime = ZonedDateTime.now();
            itemSale.setDateTime(dateTime);
        }
        // Update price
        BigDecimal quantity = itemSale.getQuantity();
        Price price = itemSale.getPrice();
        Item item = itemSale.getItem();
        Price itemPrice = item.getCurrentPrice();
        
        if (price == null) {
            BigDecimal vatExclusive = itemPrice.getVatExclusive();
            BigDecimal vatRate = itemPrice.getVatRate();
            // TODO: create a new price if necessary... or clean up on sale save?
            price = new Price();
            price.setVatExclusive(vatExclusive);
            price.setVatRate(vatRate);
        }

        SalePrice salePrice = AccountingUtils.calcSalePrice(price, quantity);

        BigDecimal total = salePrice.getBase();

        itemSale.setPrice(price);
        itemSale.setTotal(total);

        ItemSale managedItemSale = entityManager.merge(itemSale);
        Sale managedSale = managedItemSale.getSale();
        managedSale = calcSale(managedSale);

        return managedItemSale;
    }

    public void removeItemSale(ItemSale itemSale) {
        Sale sale = itemSale.getSale();
        if (sale.isClosed()) {
            throw new IllegalArgumentException("Cannot change a closed sale.");
        }
        ItemSale managedItemSale = entityManager.merge(itemSale);
        Sale managedSale = managedItemSale.getSale();
        entityManager.remove(managedItemSale);

        managedSale = calcSale(managedSale);
    }

    @Nonnull
    public List<ItemSale> findItemSales(@Nonnull ItemSaleSearch itemSaleSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemSale> query = criteriaBuilder.createQuery(ItemSale.class);
        Root<ItemSale> itemSaleRoot = query.from(ItemSale.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<ItemSale, Sale> saleJoin = itemSaleRoot.join(ItemSale_.sale);
        Join<ItemSale, Item> itemJoin = itemSaleRoot.join(ItemSale_.item);

        Company company = itemSaleSearch.getCompany();

        Path<Company> companyPath = saleJoin.get(Sale_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Sale sale = itemSaleSearch.getSale();
        if (sale != null) {
            Predicate salePredicate = criteriaBuilder.equal(saleJoin, sale);
            predicates.add(salePredicate);
        }

        Item item = itemSaleSearch.getItem();
        if (item != null) {
            Predicate itemPredicate = criteriaBuilder.equal(itemJoin, item);
            predicates.add(itemPredicate);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<ItemSale> typedQuery = entityManager.createQuery(query);

        List<ItemSale> itemSales = typedQuery.getResultList();

        return itemSales;
    }

    public Sale closeSale(Sale sale) {
        sale.setClosed(true);
        Sale managedSale = saveSale(sale);

        return managedSale;

    }

    public BigDecimal getSaleTotalPayed(Sale sale) {
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = accountingTransaction.getCompany();

        AccountingEntrySearch accountingEntrySearch = new AccountingEntrySearch();
        accountingEntrySearch.setAccountingTransaction(accountingTransaction);
        accountingEntrySearch.setCompany(company);

        List<AccountingEntry> paymentAccountingEntries = accountService.findAccountingEntries(accountingEntrySearch);

        BigDecimal totalPayed = paymentAccountingEntries.stream()
                .map(AccountingEntry::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .setScale(2);

        return totalPayed;
    }

}
