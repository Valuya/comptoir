package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.factory.LocaleTextFactory;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.thirdparty.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    public List<Account> findAccounts(@Nonnull Company company, AccountType accountType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = query.from(Account.class);

        List<Predicate> predicates = new ArrayList<>();

        Path<Company> companyPath = accountRoot.get(Account_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

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

    public void register(Company company, Employee employee) {
        Company managedCompany = entityManager.merge(company);
        Employee managedEmployee = entityManager.merge(employee);

        LocaleText stockDescription = localeTextFactory.createLocaleText();

        Map<Locale, String> stockDescriptionLocaleTextMap = stockDescription.getLocaleTextMap();
        stockDescriptionLocaleTextMap.put(Locale.ENGLISH, "Default stock");
        stockDescriptionLocaleTextMap.put(Locale.FRENCH, "Stock par défaut");

        Stock stock = new Stock();
        stock.setDescription(stockDescription);
        stock.setCompany(managedCompany);

        Stock managedStock = entityManager.merge(stock);
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
