package be.valuya.comptoir.service;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.CustomerSearch;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.model.thirdparty.Customer_;
import be.valuya.comptoir.util.pagination.CustomerColumn;
import be.valuya.comptoir.util.pagination.Pagination;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
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
            Predicate cityPredicate = createContainsPredicate(cityPath, cityContains);
            predicates.add(cityPredicate);
        }
        String emailContains = customerSearch.getEmailContains();
        if (emailContains != null) {
            Predicate mailPredicate = createContainsPredicate(emailPath, emailContains);
            predicates.add(mailPredicate);
        }
        String firstNameContains = customerSearch.getFirstNameContains();
        if (firstNameContains != null) {
            Predicate firstNamePredicate = createContainsPredicate(firstNamePath, firstNameContains);
            predicates.add(firstNamePredicate);
        }
        String lastNameContains = customerSearch.getLastNameContains();
        if (lastNameContains != null) {
            Predicate lastNamePredicate = createContainsPredicate(lastNamePath, lastNameContains);
            predicates.add(lastNamePredicate);
        }
        String notesContains = customerSearch.getNotesContains();
        if (notesContains != null) {
            Predicate notesPredicate = createContainsPredicate(notesPath, notesContains);
            predicates.add(notesPredicate);
        }
        String multiSearch = customerSearch.getMultiSearch();
        if (multiSearch != null) {
            String[] stringParts = multiSearch.split(" ");
            List<Predicate> multiPredicates = new ArrayList<>();
            for (String part : stringParts) {
                Predicate firstNamePredicate = createContainsPredicate(firstNamePath, part);
                Predicate lastNamePredicate = createContainsPredicate(lastNamePath, part);
                Predicate address1Predicate = createContainsPredicate(address1Path, part);
                Predicate address2Predicate = createContainsPredicate(address2Path, part);
                Predicate zipPredicate = createContainsPredicate(zipPath, part);
                Predicate cityPredicate = createContainsPredicate(cityPath, part);
                Predicate phone1Predicate = createContainsPredicate(phone1Path, part);
                Predicate phone2Predicate = createContainsPredicate(phone2Path, part);
                Predicate mailPredicate = createContainsPredicate(emailPath, part);
                Predicate notesPredicate = createContainsPredicate(notesPath, part);
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

    private Predicate createContainsPredicate(Path<String> path, String contains) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Expression<String> lowerPathExpression = criteriaBuilder.lower(path);
        String lowerContains = contains.toLowerCase();
        Expression<Integer> containsExpression = criteriaBuilder.locate(lowerPathExpression, lowerContains);
        Predicate containsPredicate = criteriaBuilder.greaterThan(containsExpression, 0);
        return containsPredicate;
    }

}
