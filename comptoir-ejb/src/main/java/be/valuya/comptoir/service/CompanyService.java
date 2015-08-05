package be.valuya.comptoir.service;

import be.valuya.comptoir.model.company.Company;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class CompanyService {

    @PersistenceContext
    private EntityManager entityManager;

    public Company saveCompany(Company company) {
        return entityManager.merge(company);
    }

    public Company findCompanyById(long id) {
        return entityManager.find(Company.class, id);
    }

}
