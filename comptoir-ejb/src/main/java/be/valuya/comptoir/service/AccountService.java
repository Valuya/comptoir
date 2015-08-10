package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.factory.LocaleTextFactory;
import be.valuya.comptoir.model.search.AccountSearch;
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
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private LocaleTextFactory localeTextFactory;

    @Nonnull
    public List<Account> findAccounts(@Nonnull AccountSearch accountSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = query.from(Account.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = accountSearch.getCompany();
        Path<Company> companyPath = accountRoot.get(Account_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        AccountType accountType = accountSearch.getAccountType();
        if (accountType != null) {
            Path<AccountType> accountTypePath = accountRoot.get(Account_.accountType);
            Predicate accountTypePredicate = criteriaBuilder.equal(accountTypePath, accountType);
            predicates.add(accountTypePredicate);
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Account> typedQuery = entityManager.createQuery(query);

        List<Account> accounts = typedQuery.getResultList();

        return accounts;
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

}
