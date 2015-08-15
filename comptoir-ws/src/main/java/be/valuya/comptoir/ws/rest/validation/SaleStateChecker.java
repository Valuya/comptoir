/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.commercial.Sale;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class SaleStateChecker {

    public enum SaleState {

        OPEN,
        CLOSED
    }

    public boolean checkState(SaleState expected, Sale sale) {
        SaleState state = getState(sale);
        return state == expected;
    }

    private SaleState getState(Sale sale) {
        if (sale.isClosed()) {
            return SaleState.CLOSED;
        } else {
            return SaleState.OPEN;
        }
    }

}
