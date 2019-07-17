package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.CustomerLoyaltyAccountingEntry;
import be.valuya.comptoir.model.accounting.CustomerLoyaltyAccountingEntry_;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.Sale_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.CustomerLoyaltyAccountingEntrySearch;
import be.valuya.comptoir.model.search.CustomerSearch;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.model.thirdparty.Customer_;
import be.valuya.comptoir.persistence.util.CustomerColumnPersistenceUtils;
import be.valuya.comptoir.persistence.util.PaginatedQueryService;
import be.valuya.comptoir.util.pagination.CustomerColumn;
import be.valuya.comptoir.util.pagination.Pagination;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class CustomerService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PaginatedQueryService paginatedQueryService;
    @Inject
    private LangService langService;

    public Customer saveCustomer(Customer customer) {
        Customer managedCustomer = entityManager.merge(customer);
        return managedCustomer;
    }

    public Customer findCustomerById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    public List<Customer> findCustomers(CustomerSearch customerSearch) {
        return findCustomers(customerSearch, null);
    }

    public List<Customer> findCustomers(CustomerSearch customerSearch, Pagination<Customer, CustomerColumn> pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = query.from(Customer.class);

        List<Predicate> predicates = createCustomerPredicates(customerSearch, criteriaBuilder, customerRoot);

        paginatedQueryService.applySort(pagination, customerRoot, query, column ->
                CustomerColumnPersistenceUtils.getPath(customerRoot, column)
        );
        List<Customer> customerList = paginatedQueryService.getResults(predicates, query, customerRoot, pagination);
        return customerList;
    }

    public CustomerLoyaltyAccountingEntry saveCustomerLoyaltyAccountingEntry(CustomerLoyaltyAccountingEntry accountingEntry) {
        CustomerLoyaltyAccountingEntry managedAccountingEntry = entityManager.merge(accountingEntry);
        return managedAccountingEntry;
    }

    public void removeCustomerLoyaltyAccountingEntry(CustomerLoyaltyAccountingEntry accountingEntry) {
        CustomerLoyaltyAccountingEntry managedAccountingEntry = entityManager.merge(accountingEntry);
        entityManager.remove(managedAccountingEntry);
    }

    public List<CustomerLoyaltyAccountingEntry> findCustomerLoyaltyAccountingEntries(CustomerLoyaltyAccountingEntrySearch accountingEntrySearch, Pagination pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerLoyaltyAccountingEntry> query = criteriaBuilder.createQuery(CustomerLoyaltyAccountingEntry.class);
        Root<CustomerLoyaltyAccountingEntry> accountingEntryRoot = query.from(CustomerLoyaltyAccountingEntry.class);

        List<Predicate> predicates = createCustomerLoyaltyAccountingEntryPredicates(accountingEntrySearch, criteriaBuilder, accountingEntryRoot);

        Path<ZonedDateTime> dateTimePath = accountingEntryRoot.get(CustomerLoyaltyAccountingEntry_.dateTime);
        Order dateOrder = criteriaBuilder.desc(dateTimePath);

        query.select(accountingEntryRoot);
        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(dateOrder);

        TypedQuery<CustomerLoyaltyAccountingEntry> typedQuery = entityManager.createQuery(query);
        paginatedQueryService.paginate(pagination, typedQuery);
        List<CustomerLoyaltyAccountingEntry> resultList = typedQuery.getResultList();

        return resultList;
    }

    public BigDecimal getCustomerLoyaltyAccountBalance(CustomerLoyaltyAccountingEntrySearch accountingEntrySearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = criteriaBuilder.createQuery(BigDecimal.class);
        Root<CustomerLoyaltyAccountingEntry> accountingEntryRoot = query.from(CustomerLoyaltyAccountingEntry.class);

        List<Predicate> predicates = createCustomerLoyaltyAccountingEntryPredicates(accountingEntrySearch, criteriaBuilder, accountingEntryRoot);

        Path<BigDecimal> amountPath = accountingEntryRoot.get(CustomerLoyaltyAccountingEntry_.amount);
        Expression<BigDecimal> amountSumExpression = criteriaBuilder.sum(amountPath);

        query.select(amountSumExpression);
        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(query);
        BigDecimal accountBalance = typedQuery.getSingleResult();
        return accountBalance;
    }

    private List<Predicate> createCustomerLoyaltyAccountingEntryPredicates(CustomerLoyaltyAccountingEntrySearch accountingEntrySearch, CriteriaBuilder criteriaBuilder, Root<CustomerLoyaltyAccountingEntry> accountingEntryRoot) {
        List<Predicate> predicates = new ArrayList<>();

        Company company = accountingEntrySearch.getCompany();
        Join<CustomerLoyaltyAccountingEntry, Sale> saleJoin = accountingEntryRoot.join(CustomerLoyaltyAccountingEntry_.sale);
        Path<Company> companyPath = saleJoin.get(Sale_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Customer customer = accountingEntrySearch.getCustomer();
        if (customer != null) {
            Path<Customer> customerPath = accountingEntryRoot.get(CustomerLoyaltyAccountingEntry_.customer);
            Predicate customerPredicate = criteriaBuilder.equal(customerPath, customer);
            predicates.add(customerPredicate);
        }

        Sale sale = accountingEntrySearch.getSale();
        if (sale != null) {
            Predicate salePredicate = criteriaBuilder.equal(saleJoin, sale);
            predicates.add(salePredicate);
        }

        Path<ZonedDateTime> dateTimePath = accountingEntryRoot.get(CustomerLoyaltyAccountingEntry_.dateTime);
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
        return predicates;
    }

    private List<Predicate> createCustomerPredicates(CustomerSearch customerSearch, CriteriaBuilder criteriaBuilder, Root<Customer> customerRoot) {
        List<Predicate> predicates = new ArrayList<>();

        Company company = customerSearch.getCompany();
        Path<Company> companyPath = customerRoot.get(Customer_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Path<String> address1Path = customerRoot.get(Customer_.adress1);
        Path<String> address2Path = customerRoot.get(Customer_.adress2);
        Path<String> cityPath = customerRoot.get(Customer_.city);
        Path<String> emailPath = customerRoot.get(Customer_.email);
        Path<String> firstNamePath = customerRoot.get(Customer_.firstName);
        Path<String> lastNamePath = customerRoot.get(Customer_.lastName);
        Path<String> notesPath = customerRoot.get(Customer_.notes);
        Path<String> phone1Path = customerRoot.get(Customer_.phone1);
        Path<String> phone2Path = customerRoot.get(Customer_.phone2);
        Path<String> zipPath = customerRoot.get(Customer_.zip);

        String cityContains = customerSearch.getCityContains();
        if (cityContains != null) {
            Predicate cityPredicate = langService.createContainsPredicate(cityPath, cityContains);
            predicates.add(cityPredicate);
        }
        String emailContains = customerSearch.getEmailContains();
        if (emailContains != null) {
            Predicate mailPredicate = langService.createContainsPredicate(emailPath, emailContains);
            predicates.add(mailPredicate);
        }
        String firstNameContains = customerSearch.getFirstNameContains();
        if (firstNameContains != null) {
            Predicate firstNamePredicate = langService.createContainsPredicate(firstNamePath, firstNameContains);
            predicates.add(firstNamePredicate);
        }
        String lastNameContains = customerSearch.getLastNameContains();
        if (lastNameContains != null) {
            Predicate lastNamePredicate = langService.createContainsPredicate(lastNamePath, lastNameContains);
            predicates.add(lastNamePredicate);
        }
        String notesContains = customerSearch.getNotesContains();
        if (notesContains != null) {
            Predicate notesPredicate = langService.createContainsPredicate(notesPath, notesContains);
            predicates.add(notesPredicate);
        }
        String multiSearch = customerSearch.getMultiSearch();
        if (multiSearch != null) {
            String[] stringParts = multiSearch.split(" ");
            List<Predicate> multiPredicates = new ArrayList<>();
            for (String part : stringParts) {
                Predicate firstNamePredicate = langService.createContainsPredicate(firstNamePath, part);
                Predicate lastNamePredicate = langService.createContainsPredicate(lastNamePath, part);
                Predicate address1Predicate = langService.createContainsPredicate(address1Path, part);
                Predicate address2Predicate = langService.createContainsPredicate(address2Path, part);
                Predicate zipPredicate = langService.createContainsPredicate(zipPath, part);
                Predicate cityPredicate = langService.createContainsPredicate(cityPath, part);
                Predicate phone1Predicate = langService.createContainsPredicate(phone1Path, part);
                Predicate phone2Predicate = langService.createContainsPredicate(phone2Path, part);
                Predicate mailPredicate = langService.createContainsPredicate(emailPath, part);
                Predicate notesPredicate = langService.createContainsPredicate(notesPath, part);
                Predicate multiSearchPartPredicate = criteriaBuilder.or(
                        firstNamePredicate,
                        lastNamePredicate,
                        address1Predicate,
                        address2Predicate,
                        zipPredicate,
                        cityPredicate,
                        phone1Predicate,
                        phone2Predicate,
                        mailPredicate,
                        notesPredicate);
                multiPredicates.add(multiSearchPartPredicate);
            }
            Predicate multiSearchPredicate = criteriaBuilder.and(multiPredicates.toArray(new Predicate[0]));
            predicates.add(multiSearchPredicate);
        }
        return predicates;
    }

}
