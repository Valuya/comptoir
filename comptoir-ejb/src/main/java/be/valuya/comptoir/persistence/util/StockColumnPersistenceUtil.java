/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.persistence.util;

import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.Stock_;
import be.valuya.comptoir.util.pagination.StockColumn;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 *
 * @author cghislai
 */
public class StockColumnPersistenceUtil {

    public static Path<?> getPath(From<?, Stock> stockFrom, StockColumn stockColumn) {
        switch (stockColumn) {
            case COMPANY:
                return stockFrom.get(Stock_.company);
            case ACTIVE:
                return stockFrom.get(Stock_.active);
            default:
                throw new AssertionError(stockColumn.name());

        }

    }
}
