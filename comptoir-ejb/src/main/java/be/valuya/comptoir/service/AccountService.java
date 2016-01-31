package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingEntry_;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.Balance_;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.model.cash.MoneyPile_;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.commercial.PosPaymentAccount;
import be.valuya.comptoir.model.commercial.PosPaymentAccount_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.model.search.BalanceSearch;
import be.valuya.comptoir.util.pagination.AccountColumn;
import be.valuya.comptoir.util.pagination.BalanceColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PaginatedQueryService paginatedQueryService;

    @Nonnull
    public List<Account> findAccounts(@Nonnull AccountSearch accountSearch, @CheckForNull Pagination<Account, AccountColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = query.from(Account.class);

        List<Predicate> predicates = createAccountPredicates(query, accountSearch, accountRoot);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        paginatedQueryService.applySort(pagination, accountRoot, query,
                accountColumn -> AccountColumnPersistenceUtil.getPath(accountRoot, accountColumn)
        );

        List<Account> accounts = paginatedQueryService.getResults(predicates, query, accountRoot, pagination);

        return accounts;
    }

    private List<Predicate> createAccountPredicates(CriteriaQuery<?> query, AccountSearch accountSearch, From<?, Account> accountPath) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        Company company = accountSearch.getCompany();
        Path<Company> companyPath = accountPath.get(Account_.company);

        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Boolean cash = accountSearch.getCash();
        if (cash != null) {
            Path<Boolean> cashPath = accountPath.get(Account_.cash);
            Predicate cashPredicate = criteriaBuilder.equal(cashPath, cash);
            predicates.add(cashPredicate);
        }

        AccountType accountType = accountSearch.getAccountType();
        if (accountType != null) {
            Path<AccountType> accountTypePath = accountPath.get(Account_.accountType);
            Predicate accountTypePredicate = criteriaBuilder.equal(accountTypePath, accountType);
            predicates.add(accountTypePredicate);
        }

        Pos pos = accountSearch.getPos();
        if (pos != null) {
            Subquery<PosPaymentAccount> posPaymentAccountSubquery = query.subquery(PosPaymentAccount.class);
            Root<PosPaymentAccount> posPaymentAccountRoot = posPaymentAccountSubquery.from(PosPaymentAccount.class);

            Join<PosPaymentAccount, Pos> posPaymentAccountPosJoin = posPaymentAccountRoot.join(PosPaymentAccount_.pointOfSale);
            Predicate posPaymentAccountPosPredicate = criteriaBuilder.equal(posPaymentAccountPosJoin, pos);

            Join<PosPaymentAccount, Account> posPaymentAccountAccountJoin = posPaymentAccountRoot.join(PosPaymentAccount_.account);
            Predicate posPaymentAccountAccountPredicate = criteriaBuilder.equal(posPaymentAccountAccountJoin, accountPath);

            posPaymentAccountSubquery.where(posPaymentAccountPosPredicate, posPaymentAccountAccountPredicate);

            Predicate posPredicate = criteriaBuilder.exists(posPaymentAccountSubquery);
            predicates.add(posPredicate);
        }

        return predicates;
    }

    public Account findAccountById(Long id) {
        return entityManager.find(Account.class, id);
    }

    public AccountingTransaction findAccountingTransactionById(Long id) {
        return entityManager.find(AccountingTransaction.class, id);
    }

    public AccountingEntry findAccountingEntryById(Long id) {
        return entityManager.find(AccountingEntry.class, id);
    }

    public void removeAccountingEntry(AccountingEntry accountingEntry) {
        AccountingEntry managedEntry = entityManager.merge(accountingEntry);
        entityManager.remove(managedEntry);
    }

    public Account saveAccount(Account account) {
        return entityManager.merge(account);
    }

    public AccountingEntry saveAccountingEntry(AccountingEntry accountingEntry) {
        return entityManager.merge(accountingEntry);
    }

    public List<AccountingEntry> findAccountingEntries(AccountingEntrySearch accountingEntrySearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountingEntry> query = criteriaBuilder.createQuery(AccountingEntry.class);
        Root<AccountingEntry> accountingEntryRoot = query.from(AccountingEntry.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = accountingEntrySearch.getCompany();
        Join<AccountingEntry, Company> companyJoin = accountingEntryRoot.join(AccountingEntry_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyJoin, company);
        predicates.add(companyPredicate);

        AccountSearch accountSearch = accountingEntrySearch.getAccountSearch();
        if (accountSearch != null) {
            Join<AccountingEntry, Account> accountJoin = accountingEntryRoot.join(AccountingEntry_.account);
            List<Predicate> accountPredicates = createAccountPredicates(query, accountSearch, accountJoin);
            predicates.addAll(accountPredicates);
        }

        Path<ZonedDateTime> dateTimePath = accountingEntryRoot.get(AccountingEntry_.dateTime);
        ZonedDateTime fromDateTime = accountingEntrySearch.getFromDateTime();
        if (fromDateTime != null) {
            Predicate fromDateTimePredicate = criteriaBuilder.greaterThanOrEqualTo(dateTimePath, fromDateTime);
            predicates.add(fromDateTimePredicate);
        }
        ZonedDateTime toDateTime = accountingEntrySearch.getToDateTime();
        if (toDateTime != null) {
            Predicate toDateTimePredicate = criteriaBuilder.lessThan(dateTimePath, toDateTime);
            predicates.add(toDateTimePredicate);
        }

        AccountingTransaction accountingTransaction = accountingEntrySearch.getAccountingTransaction();
        if (accountingTransaction != null) {
            Predicate accountingTransactionPredicate = criteriaBuilder.equal(accountingEntryRoot.get(AccountingEntry_.accountingTransaction), accountingTransaction);
            predicates.add(accountingTransactionPredicate);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<AccountingEntry> typedQuery = entityManager.createQuery(query);

        List<AccountingEntry> accountingEntries = typedQuery.getResultList();

        return accountingEntries;
    }

    public Balance findBalanceById(long id) {
        return entityManager.find(Balance.class, id);
    }

    public List<Balance> findBalances(BalanceSearch balanceSearch, @CheckForNull Pagination<Balance, BalanceColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Balance> query = criteriaBuilder.createQuery(Balance.class);
        Root<Balance> balanceRoot = query.from(Balance.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = balanceSearch.getCompany();
        AccountSearch accountSearch = balanceSearch.getAccountSearch();
        if (accountSearch == null) {
            accountSearch = new AccountSearch();
            accountSearch.setCompany(company);
        }
        Join<Balance, Account> accountJoin = balanceRoot.join(Balance_.account);
        List<Predicate> accountPredicates = createAccountPredicates(query, accountSearch, accountJoin);
        predicates.addAll(accountPredicates);

        Account account = balanceSearch.getAccount();
        if (account != null) {
            Predicate accountPredicate = criteriaBuilder.equal(accountJoin, account);
            predicates.add(accountPredicate);
        }

        Path<ZonedDateTime> dateTimePath = balanceRoot.get(Balance_.dateTime);
        ZonedDateTime fromDateTime = balanceSearch.getFromDateTime();
        if (fromDateTime != null) {
            Predicate fromDateTimePredicate = criteriaBuilder.greaterThanOrEqualTo(dateTimePath, fromDateTime);
            predicates.add(fromDateTimePredicate);
        }
        ZonedDateTime toDateTime = balanceSearch.getToDateTime();
        if (toDateTime != null) {
            Predicate toDateTimePredicate = criteriaBuilder.lessThan(dateTimePath, toDateTime);
            predicates.add(toDateTimePredicate);
        }

        paginatedQueryService.applySort(pagination, balanceRoot, query,
                balanceColumn -> BalanceColumnPersistenceUtil.getPath(balanceRoot, balanceColumn));

        List<Balance> balances = paginatedQueryService.getResults(predicates, query, balanceRoot, pagination);

        return balances;

    }

    public Balance saveBalance(Balance balance) {
        return entityManager.merge(balance);
    }

    public MoneyPile findMoneyPileById(long id) {
        return entityManager.find(MoneyPile.class, id);
    }

    public List<MoneyPile> findMoneyPileListForBalance(Balance balance) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MoneyPile> query = criteriaBuilder.createQuery(MoneyPile.class);
        Root<MoneyPile> root = query.from(MoneyPile.class);

        Path<Balance> balancePath = root.get(MoneyPile_.balance);
        Predicate balancePredicate = criteriaBuilder.equal(balancePath, balance);

        query.select(root);
        query.where(balancePredicate);

        TypedQuery<MoneyPile> typedQuery = entityManager.createQuery(query);
        List<MoneyPile> resultList = typedQuery.getResultList();
        return resultList;
    }

    public MoneyPile saveMoneyPile(MoneyPile moneyPile) {
        if (moneyPile.getDateTime() == null) {
            ZonedDateTime dateTime = ZonedDateTime.now();
            moneyPile.setDateTime(dateTime);
        }
        MoneyPile managedMoneyPile = entityManager.merge(moneyPile);
        managedMoneyPile = AccountingUtils.calcMoneyPile(managedMoneyPile);

        Balance balance = managedMoneyPile.getBalance();
        List<MoneyPile> moneyPiles = findMoneyPileListForBalance(balance);
        balance = AccountingUtils.calcBalance(balance, moneyPiles);
        Balance managedBalance = entityManager.merge(balance);

        return managedMoneyPile;
    }

    public Balance closeBalance(Balance balance) {
        balance.setClosed(true);
        if (balance.getDateTime() == null) {
            ZonedDateTime dateTime = ZonedDateTime.now();
            balance.setDateTime(dateTime);
        }
        return saveBalance(balance);
    }

    public void cancelOpenBalance(Balance balance) {
        if (balance.isClosed()) {
            throw new IllegalArgumentException("The balance is closed");
        }
//        List<MoneyPile> moneyPiles = findMoneyPiles();
//        moneyPiles.forEach(thid::deleteMoneyPile);
        Balance managedBalance = entityManager.merge(balance);
        entityManager.remove(managedBalance);
    }
}
