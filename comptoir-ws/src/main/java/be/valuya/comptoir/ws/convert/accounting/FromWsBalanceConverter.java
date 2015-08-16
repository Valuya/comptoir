package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.accounting.WsBalance;
import be.valuya.comptoir.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.service.AccountService;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsBalanceConverter {

    @Inject
    private FromWsAccountConverter fromWsAccountConverter;
    @EJB
    private AccountService accountService;

    public Balance convert(WsBalance wsBalance) {
        if (wsBalance == null) {
            return null;
        }
        Long id = wsBalance.getId();
        BigDecimal balanceAmount = wsBalance.getBalance();
        ZonedDateTime dateTime = wsBalance.getDateTime();
        boolean closed = wsBalance.isClosed();

        WsAccountRef accountRef = wsBalance.getAccountRef();
        Account account = fromWsAccountConverter.find(accountRef);

        Balance balance = new Balance();
        balance.setId(id);
        balance.setAccount(account);
        balance.setBalance(balanceAmount);
        balance.setDateTime(dateTime);
        balance.setClosed(closed);

        return balance;
    }

    public Balance find(WsBalanceRef balanceRef) {
        if (balanceRef == null) {
            return null;
        }
        Long id = balanceRef.getId();
        return accountService.findBalanceById(id);
    }

}
