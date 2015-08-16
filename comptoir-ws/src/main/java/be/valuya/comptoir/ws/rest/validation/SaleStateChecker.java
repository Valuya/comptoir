/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.commercial.Sale;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class SaleStateChecker {

    public void checkState(Sale sale, boolean expectedClosedState) {
        boolean closed = sale.isClosed();
        if (closed != expectedClosedState) {
            throw new BadRequestException("Unexpected sale state");
        }
    }

}
