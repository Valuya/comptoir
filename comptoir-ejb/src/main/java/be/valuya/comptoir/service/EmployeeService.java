package be.valuya.comptoir.service;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.EmployeeSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.model.thirdparty.Employee_;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class EmployeeService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private AuthService authService;

    public Optional<Employee> findEmployeeOptionalByLogin(@Nonnull String login) {
        return Optional.ofNullable(this.findEmployeeByLogin(login));
    }

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
            Employee employee = typedQuery.getSingleResult();
            return employee;
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public Employee saveEmployee(Employee employee) {
        return entityManager.merge(employee);
    }

    public void setPassword(Employee employee, String employeePassword) {
        String hashedPassword = authService.hashPassword(employeePassword);

        Employee managedEmployee = entityManager.merge(employee);
        managedEmployee.setPasswordHash(hashedPassword);
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
