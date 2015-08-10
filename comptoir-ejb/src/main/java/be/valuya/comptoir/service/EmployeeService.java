package be.valuya.comptoir.service;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.EmployeeSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.model.thirdparty.Employee_;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class EmployeeService {

    @PersistenceContext
    private EntityManager entityManager;

    @CheckForNull
    public Employee findEmployeeByLogin(@Nonnull String login) {
        if (login.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty login");
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = query.from(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        Path<String> loginPath = employeeRoot.get(Employee_.login);
        Predicate loginPredicate = criteriaBuilder.equal(loginPath, login);
        predicates.add(loginPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Employee> typedQuery = entityManager.createQuery(query);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public Employee saveEmployee(Employee employee) {
        return entityManager.merge(employee);
    }

    public void setPassword(Employee employee, String employeePassword) {
        String hashedPassword = hashPassword(employeePassword);

        Employee managedEmployee = entityManager.merge(employee);
        managedEmployee.setPasswordHash(hashedPassword);
    }

    public String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            BigInteger bigInteger = new BigInteger(1, digest);
            String hashedPassword = bigInteger.toString(16);

            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashedPassword.length() < 32) {
                hashedPassword = "0" + hashedPassword;
            }

            return hashedPassword;
        } catch (NoSuchAlgorithmException exception) {
            throw new AssertionError(exception);
        }
    }

    public String login(Employee employee, String passwordHash) {
        return "45678-test-123";
    }

    public Employee findEmployeeById(long id) {
        return entityManager.find(Employee.class, id);
    }

    public List<Employee> findEmployees(EmployeeSearch employeeSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = query.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        Path<Company> companyPath = employeeRoot.get(Employee_.company);
        Company company = employeeSearch.getCompany();
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Employee> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

}
