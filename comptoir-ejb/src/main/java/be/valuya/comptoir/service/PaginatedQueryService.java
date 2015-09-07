package be.valuya.comptoir.service;

import be.valuya.comptoir.util.pagination.Column;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class PaginatedQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public <T, C extends Column<T>> void applySort(Pagination<T, C> pagination, From<?, T> from, CriteriaQuery<T> query, Function<C, Path<?>> sortToPathFunction) {
        if (pagination != null) {
            List<Order> orders = createOrders(pagination, from, sortToPathFunction);
            query.orderBy(orders);
        }
    }

    public <T, C extends Column<T>> List<T> getResults(CriteriaQuery<T> query, Pagination<T, C> pagination) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        paginate(pagination, typedQuery);
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

    public <T, C extends Column<T>> List<Order> createOrders(Pagination<T, C> pagination, From<?, T> from, Function<C, Path<?>> sortToPathFunction) {
        List<Order> orders = pagination.getSortings()
                .stream()
                .map(sort -> createOrder(sort, sortToPathFunction))
                .collect(Collectors.toList());

        return orders;
    }

    private <T, C extends Column<T>> Order createOrder(Sort<C> sort, Function<C, Path<?>> sortToPathFunction) {
        C sortColumn = sort.getSortColumn();
        Path<?> path = sortToPathFunction.apply(sortColumn);
        Order order;
        if (sort.isAscending()) {
            order = createAscOrder(path);
        } else {
            order = createDescOrder(path);
        }
        return order;
    }

    private Order createDescOrder(Path<?> path) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.desc(path);
    }

    private Order createAscOrder(Path<?> path) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Order order = criteriaBuilder.asc(path);
        return order;
    }

}
