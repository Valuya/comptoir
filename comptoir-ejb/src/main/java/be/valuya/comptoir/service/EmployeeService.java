package be.valuya.comptoir.service;

import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.model.thirdparty.Employee_;
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

}
