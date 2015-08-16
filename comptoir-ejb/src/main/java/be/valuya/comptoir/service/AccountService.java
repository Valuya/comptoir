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
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.model.search.BalanceSearch;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
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

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;

    @Nonnull
    public List<Account> findAccounts(@Nonnull AccountSearch accountSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = query.from(Account.class);

        List<Predicate> predicates = createAccountPredicates(accountSearch, accountRoot);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Account> typedQuery = entityManager.createQuery(query);

        List<Account> accounts = typedQuery.getResultList();

        return accounts;
    }

    private List<Predicate> createAccountPredicates(AccountSearch accountSearch, From<?, Account> accountPath) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        Company company = accountSearch.getCompany();
        Path<Company> companyPath = accountPath.get(Account_.company);

        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        AccountType accountType = accountSearch.getAccountType();
        if (accountType != null) {
            Path<AccountType> accountTypePath = accountPath.get(Account_.accountType);
            Predicate accountTypePredicate = criteriaBuilder.equal(accountTypePath, accountType);
            predicates.add(accountTypePredicate);
        }

        Pos pos = accountSearch.getPos();
        if (pos != null) {
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
            List<Predicate> accountPredicates = createAccountPredicates(accountSearch, accountJoin);
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

    public List<Balance> findBalances(BalanceSearch balanceSearch) {
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
        List<Predicate> accountPredicates = createAccountPredicates(accountSearch, accountJoin);
        predicates.addAll(accountPredicates);

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

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Balance> typedQuery = entityManager.createQuery(query);

        List<Balance> balances = typedQuery.getResultList();

        return balances;

    }

    public Balance saveBalance(Balance balance) {
        return entityManager.merge(balance);
    }

    public MoneyPile findMoneyPileById(long id) {
        return entityManager.find(MoneyPile.class, id);
    }

    public MoneyPile saveMoneyPile(MoneyPile moneyPile) {
        return entityManager.merge(moneyPile);
    }

    public Balance closeBalance(Balance balance) {
        balance.setClosed(true);
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
