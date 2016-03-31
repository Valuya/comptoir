/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.service;

import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.ItemStock_;
import be.valuya.comptoir.util.pagination.ItemVariantStockColumn;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 *
 * @author cghislai
 */
public class ItemVariantStockColumnPersistenceUtil {
    public static Path<?> getPath(From<?, ItemStock> variantStockFrom, ItemVariantStockColumn stockColumn) {
        switch (stockColumn) {
            case START_DATE_TIME:
                return variantStockFrom.get(ItemStock_.startDateTime);
            case END_DATE_TIME:
                return variantStockFrom.get(ItemStock_.endDateTime);
            case ORDER:
                return variantStockFrom.get(ItemStock_.orderPosition);
            case STOCK:
                return variantStockFrom.get(ItemStock_.stock);
            case ITEM_VARIANT:
                return variantStockFrom.get(ItemStock_.itemVariant);
            case QUANTITY:
                return variantStockFrom.get(ItemStock_.quantity);
            case COMMENT:
                return variantStockFrom.get(ItemStock_.comment);
            default:
                throw  new AssertionError(stockColumn);
        }
    }

}
