package be.valuya.comptoir.service;

import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.lang.LocaleText_;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.util.LoggedUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Locale;
import java.util.Map;

@Stateless
public class LangService {

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    @LoggedUser
    private Employee employee;


    public Predicate createContainsPredicate(Path<String> path, String contains) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Expression<String> lowerPathExpression = criteriaBuilder.lower(path);
        String lowerContains = contains.toLowerCase();
        Expression<Integer> containsExpression = criteriaBuilder.locate(lowerPathExpression, lowerContains);
        Predicate containsPredicate = criteriaBuilder.greaterThan(containsExpression, 0);
        return containsPredicate;
    }

    public Predicate createLocaleTextContainsPredicate(Join<?, LocaleText> path, String contains) {
        CriteriaBuilder criteriaBuilder1 = entityManager.getCriteriaBuilder();

        Locale locale = employee.getLocale();
        MapJoin<LocaleText, Locale, String> textsJoin = path.join(LocaleText_.localeTextMap);
        Path<Locale> joinKey = textsJoin.key();
        Predicate joinLocalePredicate = criteriaBuilder1.equal(joinKey, locale);
        textsJoin.on(joinLocalePredicate);

        Path<String> valuePath = textsJoin.value();
        return createContainsPredicate(valuePath, contains);
    }

}
