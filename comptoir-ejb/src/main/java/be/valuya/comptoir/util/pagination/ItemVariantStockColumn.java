/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.util.pagination;

import be.valuya.comptoir.model.stock.ItemStock;

/**
 *
 * @author cghislai
 */
public enum ItemVariantStockColumn implements Column<ItemStock> {

    START_DATE_TIME,
    END_DATE_TIME,
    ORDER,
    STOCK,
    ITEM_VARIANT,
    QUANTITY,
    COMMENT
}
