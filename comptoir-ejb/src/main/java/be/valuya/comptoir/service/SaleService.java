package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSale_;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
import be.valuya.comptoir.model.commercial.Sale_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.model.search.ItemVariantSaleSearch;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.util.pagination.ItemVariantSaleColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;
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
import javax.persistence.criteria.Join;
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
    @EJB
    private PaginatedQueryService paginatedQueryService;


    public Sale createSale(Stock stock, Sale sale, List<ItemVariantSale> itemSales) {
        Sale managedSale = entityManager.merge(sale);

        for (ItemVariantSale itemSale : itemSales) {
            ItemVariantSale managedItemSale = entityManager.merge(itemSale);
            managedItemSale.setSale(managedSale);
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            stockService.adaptStockFromItemSale(zonedDateTime, stock, managedItemSale, StockChangeType.SALE, null);
        }

        // TODO: create accounting entry
        return managedSale;
    }

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
        List<ItemVariantSale> itemSaleList = findItemSales(sale);
        for (ItemVariantSale itemSale : itemSaleList) {
            removeItemSale(itemSale);
        }
        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        Company company = sale.getCompany();
        AccountingEntrySearch accountingEntrySearch = new AccountingEntrySearch();
        accountingEntrySearch.setAccountingTransaction(accountingTransaction);
        accountingEntrySearch.setCompany(company);
        List<AccountingEntry> accountingEntries = accountService.findAccountingEntries(accountingEntrySearch);
        for (AccountingEntry accountingEntry : accountingEntries) {
            accountService.removeAccountingEntry(accountingEntry);
        }
        
        Sale managedSale = entityManager.merge(sale);
        entityManager.remove(managedSale);
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

        ItemVariantSale managedItemSale = entityManager.merge(itemSale);
        Sale managedSale = managedItemSale.getSale();
        managedSale = calcSale(managedSale);

        return managedItemSale;
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
                (itemVarianSaleColumn)->ItemVariantSaleColumnPersistenceUtil.getPath(itemSaleRoot, itemVarianSaleColumn));
        
        List<ItemVariantSale> itemSales = paginatedQueryService.getResults(predicates, query, itemSaleRoot, pagination);
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
                .setScale(4);

        return totalPayed;
    }

}
