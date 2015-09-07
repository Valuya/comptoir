package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariant_;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.util.pagination.ItemVariantColumn;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemVariantColumnPersistenceUtil {

    public static List<Order> createOrdersFromSortings(CriteriaBuilder criteriaBuilder, Root<ItemVariant> itemRoot, List<Sort<ItemVariantColumn>> sortings) {
        if (sortings == null) {
            return new ArrayList<>();
        }
        List<Order> orders = sortings.stream().map(
                sorting -> createOrderFromSorting(criteriaBuilder, itemRoot, sorting))
                .collect(Collectors.toList());
        return orders;
    }

    public static Order createOrderFromSorting(CriteriaBuilder criteriaBuilder, Root<ItemVariant> itemRoot, Sort<ItemVariantColumn> sorting) {
        ItemVariantColumn itemColumn = sorting.getSortColumn();
        Path<?> path = getPath(itemRoot, itemColumn);

        Order order;
        if (sorting.isAscending()) {
            order = criteriaBuilder.asc(path);
        } else {
            order = criteriaBuilder.desc(path);
        }
        return order;
    }

    public static Path<?> getPath(From<?, ItemVariant> itemVariantFrom, ItemVariantColumn itemVariantColumn) {
        Join<ItemVariant, Item> itemJoin = itemVariantFrom.join(ItemVariant_.item);
        switch (itemVariantColumn) {
            case NAME: {
                return itemJoin.get(Item_.name);
            }
            case DESCRIPTION: {
                return itemJoin.get(Item_.description);
            }
            default:
                throw new AssertionError(itemVariantColumn.name());
        }
    }

}
