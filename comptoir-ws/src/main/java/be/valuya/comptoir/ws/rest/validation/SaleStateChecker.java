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

    public void checkState(Sale sale, boolean expectedClosed) {
        boolean closed = sale.isClosed();
        if (closed != expectedClosed) {
            throw new BadRequestException("Unexpected sale state");
        }
    }

}
