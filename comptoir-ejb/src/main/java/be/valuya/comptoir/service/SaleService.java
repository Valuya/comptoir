package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.*;
import be.valuya.comptoir.model.commercial.*;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.event.SaleRemovedEvent;
import be.valuya.comptoir.model.event.SaleUpdateEvent;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.search.*;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.persistence.util.ItemVariantSaleColumnPersistenceUtil;
import be.valuya.comptoir.persistence.util.PaginatedQueryService;
import be.valuya.comptoir.persistence.util.SaleColumnPersistenceUtils;
import be.valuya.comptoir.util.pagination.ItemVariantSaleColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
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
    @EJB
    private CustomerService customerService;
    @EJB
    private PaginatedQueryService paginatedQueryService;
    @Inject
    private Event<SaleUpdateEvent> saleUpdateEvent;
    @Inject
    private Event<SaleRemovedEvent> saleRemovedEvent;


    @Nonnull
    public List<ItemVariantSale> findItemSales(@Nonnull Sale sale) {
        if (sale.getId() == null) {
            // new sale, no ItemSale list
            return Arrays.asList();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemVariantSale> query = criteriaBuilder.createQuery(ItemVariantSale.class);

        Root<ItemVariantSale> itemSaleRoot = query.from(ItemVariantSale.class);
        Path<Sale> salePath = itemSaleRoot.get(ItemVariantSale_.sale);
        Predicate salePredicate = criteriaBuilder.equal(salePath, sale);

        query.where(salePredicate);

        TypedQuery<ItemVariantSale> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    @Nonnull
    public List<AccountingEntry> findPaymentAccountingEntries(@Nonnull Sale sale) {
        if (sale.getId() == null) {
            return new ArrayList<>();
        }
        @NotNull AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = sale.getCompany();

        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setAccountType(AccountType.PAYMENT);
        accountSearch.setCompany(company);

        AccountingEntrySearch accountingEntrySearch = new AccountingEntrySearch();
        accountingEntrySearch.setAccountingTransaction(accountingTransaction);
        accountingEntrySearch.setAccountSearch(accountSearch);
        accountingEntrySearch.setCompany(company);
        List<AccountingEntry> accountingEntries = accountService.findAccountingEntries(accountingEntrySearch);
        return accountingEntries;
    }

    public Sale calcSale(Sale sale) {
        List<ItemVariantSale> itemSales = findItemSales(sale);

        return AccountingUtils.calcSale(sale, itemSales);
    }

    public void createSaleAccountingEntries(Sale sale, List<ItemVariantSale> itemSales) {
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

    private AccountingEntry createItemSaleAccountingEntry(ItemVariantSale itemSale) {
        Sale sale = itemSale.getSale();
        Company company = sale.getCompany();
        ZonedDateTime dateTime = ZonedDateTime.now();

        Customer customer = sale.getCustomer();
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();

        Price price = itemSale.getPrice();
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal vatRate = price.getVatRate();
        BigDecimal productCredit = vatExclusive.negate();

        ItemVariant itemVariant = itemSale.getItemVariant();
        Item item = itemVariant.getItem();
        LocaleText description = item.getDescription();

        List<AttributeValue> attributeValues = itemVariant.getAttributeValues();
        StringBuilder attributeValueDescriptionBuilder = new StringBuilder();
        // TODO: add all attributes

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
        Customer customer = sale.getCustomer();
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
        saleDebitAccountingEntry.setCustomer(customer); // or null?
//        saleDebitAccountingEntry.setVatAccountingEntry(vatAccountingEntry);

        return saleDebitAccountingEntry;
    }

    public AccountingEntry addPayment(Sale sale, AccountingEntry paymentAccountingEntry) {
        List<ItemVariantSale> itemSales = findItemSales(sale);
        Sale adjustedSale = AccountingUtils.calcSale(sale, itemSales);

        ZonedDateTime dateTime = ZonedDateTime.now();
        AccountingEntry saleDebitAccountingEntry = createSaleDebitAccountingEntry(adjustedSale, paymentAccountingEntry, dateTime);
        AccountingEntry managedSaleDebitAccountingEntry = entityManager.merge(saleDebitAccountingEntry);

        saleUpdateEvent.fireAsync(new SaleUpdateEvent(adjustedSale));
        return managedSaleDebitAccountingEntry;
    }

    public void deletePayment(Sale sale, AccountingEntry paymentAccountingEntry) {
        if (sale.isClosed()) {
            throw new IllegalStateException();
        }
        @NotNull AccountingTransaction saleTransaction = sale.getAccountingTransaction();
        @NotNull AccountingTransaction paymentTransaction = paymentAccountingEntry.getAccountingTransaction();
        if (saleTransaction.equals(paymentTransaction)) {
            accountService.removeAccountingEntry(paymentAccountingEntry);
            saleUpdateEvent.fireAsync(new SaleUpdateEvent(sale));
        } else {
            throw new IllegalStateException("Invalid transaction");
        }
    }

    public Sale pay(Sale sale, List<ItemVariantSale> itemSales, List<AccountingEntry> paymentAccountingEntryList, boolean close) {
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
        saleUpdateEvent.fireAsync(new SaleUpdateEvent(managedSale));
        return managedSale;
    }

    @Nonnull
    public BigDecimal calcPayBackAmount(@Nonnull Sale sale, @Nonnull List<ItemVariantSale> itemSales, @Nonnull List<AccountingEntry> paymentAccountingEntryList) {
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

    public ItemVariantSale findItemSaleById(Long saleId) {
        return entityManager.find(ItemVariantSale.class, saleId);
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
        paginatedQueryService.applySort(pagination, saleRoot, query,
                saleColumn -> SaleColumnPersistenceUtils.getPath(saleRoot, saleColumn));

        List<Sale> results = paginatedQueryService.getResults(predicates, query, saleRoot, pagination);
        return results;
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
        Path<ZonedDateTime> dateTimePath = saleRoot.get(Sale_.dateTime);
        ZonedDateTime fromDateTime = saleSearch.getFromDateTime();
        if (fromDateTime != null) {
            Predicate fromDateTimePredicate = criteriaBuilder.greaterThanOrEqualTo(dateTimePath, fromDateTime);
            predicates.add(fromDateTimePredicate);
        }
        ZonedDateTime toDateTime = saleSearch.getToDateTime();
        if (toDateTime != null) {
            Predicate toDateTimePredicate = criteriaBuilder.lessThan(dateTimePath, toDateTime);
            predicates.add(toDateTimePredicate);
        }
        Customer customer = saleSearch.getCustomer();
        if (customer != null) {
            Path<Customer> customerPath = saleRoot.get(Sale_.customer);
            Predicate customerPredicate = criteriaBuilder.equal(customerPath, customer);
            predicates.add(customerPredicate);
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
        updateCustomerLoyaltyEntry(sale);

        Sale managedSale = entityManager.merge(sale);
        managedSale = calcSale(managedSale);
        saleUpdateEvent.fireAsync(new SaleUpdateEvent(managedSale));

        return managedSale;
    }

    public void cancelOpenSale(Sale sale) {
        if (sale.isClosed()) {
            throw new IllegalArgumentException("The sale is closed");
        }
        List<ItemVariantSale> itemSaleList = findItemSales(sale);
        itemSaleList.forEach(this::removeItemSale);

        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = sale.getCompany();
        AccountingEntrySearch accountingEntrySearch = new AccountingEntrySearch();
        accountingEntrySearch.setAccountingTransaction(accountingTransaction);
        accountingEntrySearch.setCompany(company);
        List<AccountingEntry> accountingEntries = accountService.findAccountingEntries(accountingEntrySearch);
        for (AccountingEntry accountingEntry : accountingEntries) {
            accountService.removeAccountingEntry(accountingEntry);
        }

        Long saleId = sale.getId();
        Sale managedSale = entityManager.merge(sale);
        entityManager.remove(managedSale);
        saleRemovedEvent.fireAsync(new SaleRemovedEvent(saleId));
    }

    public ItemVariantSale saveItemSale(ItemVariantSale itemSale) {
        ZonedDateTime dateTime = itemSale.getDateTime();
        if (dateTime == null) {
            dateTime = ZonedDateTime.now();
            itemSale.setDateTime(dateTime);
        }
        // Update price
        ItemVariant itemVariant = itemSale.getItemVariant();

        // TODO: create a new price if necessary... or clean up on sale save?
        Price specificPrice = itemSale.getPrice();
        if (specificPrice == null) {
            Item item = itemVariant.getItem();
            Price defaultItemPrice = item.getCurrentPrice();

            specificPrice = new Price();
            BigDecimal vatExclusive = defaultItemPrice.getVatExclusive();
            BigDecimal vatRate = defaultItemPrice.getVatRate();

            Pricing pricing = itemVariant.getPricing();
            BigDecimal pricingAmount = itemVariant.getPricingAmount();
            switch (pricing) {
                case ABSOLUTE: {
                    vatExclusive = pricingAmount;
                }
                break;
                case ADD_TO_BASE: {
                    vatExclusive = vatExclusive.add(pricingAmount);
                }
                break;
                case PARENT_ITEM: {
                    // just ignore pricingAmount
                }
                break;
                default:
                    throw new AssertionError();
            }

            specificPrice.setVatExclusive(vatExclusive);
            specificPrice.setVatRate(vatRate);
        }

        BigDecimal quantity = itemSale.getQuantity();
        SalePrice salePrice = AccountingUtils.calcSalePrice(specificPrice, quantity);

        BigDecimal total = salePrice.getBase();

        itemSale.setPrice(specificPrice);
        itemSale.setTotal(total);

        // Assign a stock
        try {
            assignStockToVariantSale(itemSale);
        } catch (IllegalStateException e) {
            throw new EJBException(e);
        }

        ItemVariantSale managedItemSale = entityManager.merge(itemSale);
        entityManager.flush();
        Sale managedSale = managedItemSale.getSale();
        managedSale = calcSale(managedSale);
        saleUpdateEvent.fireAsync(new SaleUpdateEvent(managedSale));
        return managedItemSale;
    }

    private void assignStockToVariantSale(ItemVariantSale itemSale) throws IllegalStateException {
        if (itemSale.getStock() != null) {
            return;
        }
        Sale sale = itemSale.getSale();
        Company company = sale.getCompany();

        StockSearch stockSearch = new StockSearch();
        stockSearch.setCompany(company);
        stockSearch.setActive(true);

        List<Stock> stocks = stockService.findStocks(stockSearch, null);
        if (stocks == null || stocks.isEmpty()) {
            throw new IllegalStateException("No active stock for company " + company.getId());
        }
        Stock firstStock = stocks.get(0);
        itemSale.setStock(firstStock);
    }

    public void removeItemSale(ItemVariantSale itemSale) {
        Sale sale = itemSale.getSale();
        if (sale.isClosed()) {
            throw new IllegalArgumentException("Cannot change a closed sale.");
        }
        ItemVariantSale managedItemSale = entityManager.merge(itemSale);
        Sale managedSale = managedItemSale.getSale();
        entityManager.remove(managedItemSale);

        managedSale = calcSale(managedSale);
        saleUpdateEvent.fireAsync(new SaleUpdateEvent(managedSale));
    }

    @Nonnull
    public List<ItemVariantSale> findItemSales(@Nonnull ItemVariantSaleSearch itemSaleSearch, @CheckForNull Pagination<ItemVariantSale, ItemVariantSaleColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemVariantSale> query = criteriaBuilder.createQuery(ItemVariantSale.class);
        Root<ItemVariantSale> itemSaleRoot = query.from(ItemVariantSale.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<ItemVariantSale, Sale> saleJoin = itemSaleRoot.join(ItemVariantSale_.sale);
        Join<ItemVariantSale, ItemVariant> itemJoin = itemSaleRoot.join(ItemVariantSale_.itemVariant);

        Company company = itemSaleSearch.getCompany();

        Path<Company> companyPath = saleJoin.get(Sale_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Sale sale = itemSaleSearch.getSale();
        if (sale != null) {
            Predicate salePredicate = criteriaBuilder.equal(saleJoin, sale);
            predicates.add(salePredicate);
        }

        ItemVariant itemVariant = itemSaleSearch.getItemVariant();
        if (itemVariant != null) {
            Predicate itemPredicate = criteriaBuilder.equal(itemJoin, itemVariant);
            predicates.add(itemPredicate);
        }

        paginatedQueryService.applySort(pagination, itemSaleRoot, query,
                (itemVarianSaleColumn) -> ItemVariantSaleColumnPersistenceUtil.getPath(itemSaleRoot, itemVarianSaleColumn));

        List<ItemVariantSale> itemSales = paginatedQueryService.getResults(predicates, query, itemSaleRoot, pagination);
        return itemSales;
    }

    public Sale closeSale(Sale sale) {
        sale.setClosed(true);
        calcSale(sale);
        Sale managedSale = saveSale(sale);

        List<ItemVariantSale> itemSales = findItemSales(managedSale);

        // Add stock entries
        // Add an order position as they will be created at once
        // and maybe multiple entries for a single variant (multipleSale items)
        int orderPosition = 0;
        for (ItemVariantSale itemSale : itemSales) {
            ItemVariantSale managedItemSale = entityManager.merge(itemSale);
            managedItemSale.setSale(managedSale);
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            ItemStock itemStock = stockService.adaptStockFromItemSale(zonedDateTime, managedItemSale, orderPosition, null);
            orderPosition++;
        }

        BigDecimal saleTotalPayed = getSaleTotalPayed(sale);
        @NotNull BigDecimal vatExclusiveAmount = sale.getVatExclusiveAmount();
        @NotNull BigDecimal vatAmount = sale.getVatAmount();
        BigDecimal saleTotal = vatExclusiveAmount.add(vatAmount);
        BigDecimal saleRemaining = saleTotal.min(saleTotalPayed);

        // TODO: create accounting entry
        if (saleRemaining.compareTo(BigDecimal.ZERO) > 0) {
            // TODO: add discount accounting entry
        } else if (saleRemaining.compareTo(BigDecimal.ZERO) < 0) {
            // TODO: add reimbursed accounting entry
        }

        saleUpdateEvent.fireAsync(new SaleUpdateEvent(managedSale));

        return managedSale;

    }

    public Sale reopenSale(Sale sale) {
        List<ItemVariantSale> itemSales = findItemSales(sale);
        // Remove stock entries
        for (ItemVariantSale variantSale : itemSales) {
            try {
                stockService.adaptStockForRemovedItemSale(variantSale);
            } catch (IllegalStateException e) {
                throw new EJBException(e);
//                switch (e.getErrorCode()) {
//                    case StockService.ERROR_MISSING_STOCK:
//                         Discard sales which do not have stocks (yet)
//                        Logger logger = Logger.getLogger(getClass().getName());
//                        logger.log(Level.WARNING, "Missing stock entry for an itemvariant sale", e);
//                        break;
//                    default: {
//                    }
//                }
            }
        }

        // TODO: remove accounting entries
        sale.setClosed(false);
        Sale managedSale = saveSale(sale);
        return managedSale;
    }

    private void updateCustomerLoyaltyEntry(Sale sale) {
        // Remove existing
        try {
            CustomerLoyaltyAccountingEntry customerLoyaltyEntry = findSaleCustomerLoyaltyEntry(sale);
            Optional.ofNullable(customerLoyaltyEntry)
                    .ifPresent(this::removeCustomerLoyaltyAccountingEntry);
        } catch (IllegalStateException e) {
            throw new EJBException("Error while fetching customer loyalty entry for sale #" + sale.getId());
        }

        // Create new one
        List<ItemVariantSale> itemSales = findItemSales(sale);
        createSaleCustomerLoyaltyAccountingEntry(sale, itemSales);
    }

    private void createSaleCustomerLoyaltyAccountingEntry(Sale sale, List<ItemVariantSale> itemSales) {
        // Check itemSales included
        itemSales = itemSales.stream()
                .map(item -> {
                    if (item.getIncludeCustomerLoyalty() != null) {
                        return item;
                    }
                    boolean shouldIncludeCustomerLoyalty = AccountingUtils.shouldIncludeCustomerLoyalty(item);
                    item.setIncludeCustomerLoyalty(shouldIncludeCustomerLoyalty);
                    return saveItemSale(item);
                })
                .collect(Collectors.toList());
        // Create entry
        CustomerLoyaltyAccountingEntry accountingEntry = AccountingUtils.calcCustomerLoyalty(sale, itemSales);
        Optional.ofNullable(accountingEntry)
                .ifPresent(customerService::saveCustomerLoyaltyAccountingEntry);
    }

    @CheckForNull
    private CustomerLoyaltyAccountingEntry findSaleCustomerLoyaltyEntry(Sale sale) throws IllegalStateException {
        CustomerLoyaltyAccountingEntrySearch accountingEntrySearch = new CustomerLoyaltyAccountingEntrySearch();
        Company company = sale.getCompany();
        accountingEntrySearch.setCompany(company);
        accountingEntrySearch.setSale(sale);
        Pagination pagination = new Pagination(0, 2, null);
        List<CustomerLoyaltyAccountingEntry> customerLoyaltyAccountingEntries = customerService.findCustomerLoyaltyAccountingEntries(accountingEntrySearch, pagination);
        if (customerLoyaltyAccountingEntries.size() > 1) {
            throw new IllegalStateException("Multiple customer loyality entries for sale #" + sale.getId());
        }
        return customerLoyaltyAccountingEntries
                .stream()
                .findFirst()
                .orElse(null);
    }

    private void removeCustomerLoyaltyAccountingEntry(CustomerLoyaltyAccountingEntry customerLoyaltyAccountingEntry) {
        customerService.removeCustomerLoyaltyAccountingEntry(customerLoyaltyAccountingEntry);
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
                .setScale(4);

        return totalPayed;
    }

    public SalePrice getSalesTotalPayed(SaleSearch saleSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<AccountingEntry> accountingEntryRoot = query.from(AccountingEntry.class);

        Subquery<Long> transactionIdSubquery = query.subquery(Long.class);
        Root<Sale> saleRoot = transactionIdSubquery.from(Sale.class);
        List<Predicate> salesPredicate = applySaleSearch(saleSearch, saleRoot, criteriaBuilder);
        Path<AccountingTransaction> accountingTransactionPath = saleRoot.get(Sale_.accountingTransaction);
        Path<Long> accountingTransactionId = accountingTransactionPath.get(AccountingTransaction_.id);
        transactionIdSubquery.select(accountingTransactionId);
        transactionIdSubquery.where(salesPredicate.toArray(new Predicate[0]));

        Path<AccountingTransaction> transactionPath = accountingEntryRoot.get(AccountingEntry_.accountingTransaction);
        Path<Long> transactionIdPath = transactionPath.get(AccountingTransaction_.id);
        Predicate transactionPredicate = transactionIdPath.in(transactionIdSubquery);

        Path<BigDecimal> amountPath = accountingEntryRoot.get(AccountingEntry_.amount);
        Join<AccountingEntry, AccountingEntry> vatAccountingEntryJoin = accountingEntryRoot.join(AccountingEntry_.vatAccountingEntry, JoinType.LEFT);
        Path<BigDecimal> vatAmountPath = vatAccountingEntryJoin.get(AccountingEntry_.amount);

        query.multiselect(amountPath, vatAmountPath);
        query.where(transactionPredicate);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        SalePrice totalPayed = typedQuery.getResultList()
                .stream()
                .map((Tuple tuple) -> {
                    BigDecimal amount = tuple.get(amountPath);
                    BigDecimal vatAmount = tuple.get(vatAmountPath);
                    // TODO: return vat amount
                    return new SalePrice(amount, vatAmount);
                })
                .reduce(new SalePrice(), (SalePrice price1, SalePrice price2) -> {
                    BigDecimal base1 = Optional.ofNullable(price1.getBase()).orElse(BigDecimal.ZERO);
                    BigDecimal base2 = Optional.ofNullable(price2.getBase()).orElse(BigDecimal.ZERO);
                    BigDecimal taxes1 = Optional.ofNullable(price1.getTaxes()).orElse(BigDecimal.ZERO);
                    BigDecimal taxes2 = Optional.ofNullable(price2.getTaxes()).orElse(BigDecimal.ZERO);
                    BigDecimal newBase = base1.add(base2).setScale(4);
                    BigDecimal newTaxes = taxes1.add(taxes2).setScale(4);
                    SalePrice newPrice = new SalePrice(newBase, newTaxes);
                    return newPrice;
                });
        return totalPayed;
    }

}
