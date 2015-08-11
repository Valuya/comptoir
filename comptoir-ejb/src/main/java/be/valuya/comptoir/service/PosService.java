package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.commercial.Pos_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.PosSearch;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
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
public class PosService {

    @PersistenceContext
    private EntityManager entityManager;

    @Nonnull
    public List<Pos> findPosList(@Nonnull PosSearch posSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pos> query = criteriaBuilder.createQuery(Pos.class);
        Root<Pos> posRoot = query.from(Pos.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = posSearch.getCompany();
        Path<Company> companyPath = posRoot.get(Pos_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Pos> typedQuery = entityManager.createQuery(query);

        List<Pos> poss = typedQuery.getResultList();

        return poss;
    }

    public Pos findPosById(Long id) {
        return entityManager.find(Pos.class, id);
    }

    public Pos savePos(Pos pos) {
        return entityManager.merge(pos);
    }

}
