package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemColumnPersistenceUtil {

    public static List<Order> createOrdersFromSortings(CriteriaBuilder criteriaBuilder, Root<Item> itemRoot, List<Sort<ItemColumn>> sortings) {
        if (sortings == null) {
            return new ArrayList<>();
        }
        List<Order> orders = sortings.stream().map(
                sorting -> createOrderFromSorting(criteriaBuilder, itemRoot, sorting))
                .collect(Collectors.toList());
        return orders;
    }

    public static Order createOrderFromSorting(CriteriaBuilder criteriaBuilder, Root<Item> itemRoot, Sort<ItemColumn> sorting) {
        ItemColumn itemColumn = sorting.getSortColumn();
        Path<?> path = getPath(itemRoot, itemColumn);

        Order order;
        if (sorting.isAscending()) {
            order = criteriaBuilder.asc(path);
        } else {
            order = criteriaBuilder.desc(path);
        }
        return order;
    }

    public static Path<?> getPath(Root<Item> itemRoot, ItemColumn itemColumn) {
        switch (itemColumn) {
            case NAME:
                return itemRoot.get(Item_.name);
            case DESCRIPTION:
                return itemRoot.get(Item_.description);
            default:
                throw new AssertionError(itemColumn.name());
        }
    }

}
