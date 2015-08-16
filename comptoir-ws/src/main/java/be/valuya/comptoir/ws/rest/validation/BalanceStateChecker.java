package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.cash.Balance;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class BalanceStateChecker {

    public void checkState(Balance balance, boolean expectedClosed) {
        boolean closed = balance.isClosed();
        if (closed != expectedClosed) {
            throw new BadRequestException("Unexpected balance state");
        }
    }

}
