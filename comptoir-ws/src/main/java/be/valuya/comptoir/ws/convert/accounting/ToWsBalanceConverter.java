package be.valuya.comptoir.ws.convert.balanceing;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.accounting.WsBalance;
import be.valuya.comptoir.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountConverter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 */
@ApplicationScoped
public class ToWsBalanceConverter {

    @Inject
    private ToWsAccountConverter toWsAccountConverter;

    public WsBalance convert(Balance balance) {
        if (balance == null) {
            return null;
        }
        Long id = balance.getId();
        BigDecimal balanceAmount = balance.getBalance();
        String comment = balance.getComment();
        ZonedDateTime dateTime = balance.getDateTime();
        boolean closed = balance.isClosed();

        Account account = balance.getAccount();
        WsAccountRef accountRef = toWsAccountConverter.reference(account);

        WsBalance wsBalance = new WsBalance();
        wsBalance.setId(id);
        wsBalance.setAccountRef(accountRef);
        wsBalance.setBalance(balanceAmount);
        wsBalance.setComment(comment);
        wsBalance.setDateTime(dateTime);
        wsBalance.setClosed(closed);

        return wsBalance;
    }

    public WsBalanceRef reference(Balance balance) {
        Long id = balance.getId();
        WsBalanceRef wsBalanceRef = new WsBalanceRef(id);
        return wsBalanceRef;
    }

}
