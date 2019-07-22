package be.valuya.comptoir.persistence.util;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.util.pagination.ItemColumn;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemColumnPersistenceUtil {

    public static Path<?> getPath(From<?, Item> itemFrom, ItemColumn itemColumn) {
        switch (itemColumn) {
            case NAME:
                return itemFrom.get(Item_.name);
            case DESCRIPTION:
                return itemFrom.get(Item_.description);
            default:
                throw new AssertionError(itemColumn.name());
        }
    }

}
