package be.valuya.comptoir.util.pagination;

import be.valuya.comptoir.model.commercial.ItemVariantSale;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public enum ItemVariantSaleColumn implements Column<ItemVariantSale> {

    NAME,
    DESCRIPTION,
    VAT_EXCLUSIVE,
    QUANTITY,
    TOTAL,
    COMMENT;
}
