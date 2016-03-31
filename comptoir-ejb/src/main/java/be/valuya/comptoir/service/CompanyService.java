package be.valuya.comptoir.service;

import be.valuya.comptoir.model.company.Company;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class CompanyService {

    @PersistenceContext
    private EntityManager entityManager;

    public Long countCompanies() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Company> companyRoot = query.from(Company.class);

        Expression<Long> companyCount = criteriaBuilder.count(companyRoot);
        query.select(companyCount);

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }

    public Company saveCompany(Company company) {
        return entityManager.merge(company);
    }

    public Company findCompanyById(long id) {
        return entityManager.find(Company.class, id);
    }

}
