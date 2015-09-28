package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariantSale_;
import be.valuya.comptoir.model.commercial.ItemVariant_;
import be.valuya.comptoir.model.commercial.Item_;
import be.valuya.comptoir.model.commercial.Price_;
import be.valuya.comptoir.util.pagination.ItemVariantSaleColumn;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemVariantSaleColumnPersistenceUtil {

 
    public static Path<?> getPath(From<?, ItemVariantSale> variantSaleFrom, ItemVariantSaleColumn variantSaleColumn) {
        Path<ItemVariant> variantPath = variantSaleFrom.get(ItemVariantSale_.itemVariant);
        switch (variantSaleColumn) {
            case NAME: {
                return variantPath.get(ItemVariant_.item).get(Item_.name);
            }
            case DESCRIPTION: {
                return variantPath.get(ItemVariant_.item).get(Item_.description);
            }
            case VAT_EXCLUSIVE: {
                return variantSaleFrom.get(ItemVariantSale_.price).get(Price_.vatExclusive);
            }
            case QUANTITY: {
                return variantSaleFrom.get(ItemVariantSale_.quantity);
            }
            case TOTAL:{
                return variantSaleFrom.get(ItemVariantSale_.total);
            }
            case COMMENT: {
                return variantSaleFrom.get(ItemVariantSale_.comment);
            }
            default:
                throw new AssertionError(variantSaleColumn.name());
        }
    }

}
