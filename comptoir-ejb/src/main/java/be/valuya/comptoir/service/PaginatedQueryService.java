package be.valuya.comptoir.service;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class PaginatedQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public <T, C extends Column<T>> List<T> getResults(List<Predicate> predicates, CriteriaQuery<T> query, Root<T> root, Pagination<T, C> pagination) {
        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        List<T> items = getResults(query, root, predicates, pagination);
        return items;
    }

    public <T, C extends Column<T>> void applySort(Pagination<T, C> pagination, From<?, T> from, CriteriaQuery<T> query, Function<C, Expression<?>> sortToPathFunction) {
        if (pagination != null) {
            List<Order> orders = createOrders(pagination, from, sortToPathFunction);
            query.orderBy(orders);
        }
    }

    private <T, C extends Column<T>> List<T> getResults(CriteriaQuery<T> query, Root<T> root, List<Predicate> predicates, Pagination<T, C> pagination) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        paginate(pagination, typedQuery);

        long allResultCount = countResults(root, predicates);
        if (pagination != null) {
            pagination.setAllResultCount(allResultCount);
        }

        return typedQuery.getResultList();
    }

    public <T, C extends Column<T>> void paginate(Pagination<T, C> pagination, TypedQuery<T> typedQuery) {
        if (pagination != null) {
            int offset = pagination.getOffset();
            int maxResults = pagination.getMaxResults();
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(maxResults);
        }
    }

    private <T, C extends Column<T>> List<Order> createOrders(Pagination<T, C> pagination, From<?, T> from, Function<C, Expression<?>> sortToPathFunction) {
        List<Sort<C>> sortings = pagination.getSortings();
        if (sortings == null) {
            return Arrays.asList();
        }
        List<Order> orders = sortings
                .stream()
                .map(sort -> createOrder(sort, sortToPathFunction))
                .collect(Collectors.toList());

        return orders;
    }

    private <T, C extends Column<T>> Order createOrder(Sort<C> sort, Function<C, Expression<?>> sortToPathFunction) {
        C sortColumn = sort.getSortColumn();
        Expression<?> path = sortToPathFunction.apply(sortColumn);
        Order order;
        if (sort.isAscending()) {
            order = createAscOrder(path);
        } else {
            order = createDescOrder(path);
        }
        return order;
    }

    private Order createDescOrder(Expression<?> path) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.desc(path);
    }

    private Order createAscOrder(Expression<?> path) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Order order = criteriaBuilder.asc(path);
        return order;
    }

    private <T, C extends Object & Column<T>> long countResults(Root<T> root, List<Predicate> predicates) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);

        query.getRoots().add(root);

        Expression<Long> countExpression = criteriaBuilder.count(root);
        query.select(countExpression);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            return 0;
        }
    }

}
