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
