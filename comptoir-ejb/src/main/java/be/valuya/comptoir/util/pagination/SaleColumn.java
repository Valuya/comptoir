package be.valuya.comptoir.util.pagination;

import be.valuya.comptoir.model.commercial.Sale;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public enum SaleColumn implements Column<Sale> {

    DATETIME,
    VAT_EXCLUSIVE,
    VAT_AMOUNT

}
