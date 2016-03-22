package be.valuya.comptoir.service;

import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.model.company.Country_;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 * Created by cghislai on 22/03/16.
 */
@Stateless
public class CountryService {
    @PersistenceContext
    private EntityManager entityManager;


    public Country getCountryByCode(String code) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Country> query = criteriaBuilder.createQuery(Country.class);
        Root<Country> countryRoot = query.from(Country.class);

        Path<String> codePath = countryRoot.get(Country_.code);
        Predicate codePredicate = criteriaBuilder.equal(codePath, code);

        query.where(codePredicate);
        TypedQuery<Country> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }

    public Country saveCountry(Country country) {
        Country managedCountry = entityManager.merge(country);
        return managedCountry;
    }
}
