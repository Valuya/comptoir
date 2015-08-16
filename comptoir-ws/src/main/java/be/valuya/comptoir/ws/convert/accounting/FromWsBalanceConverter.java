package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.accounting.WsBalance;
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

        WsAccountRef accountRef = wsBalance.getAccountRef();
        Account account = fromWsAccountConverter.find(accountRef);

        Balance balance = new Balance();
        balance.setId(id);
        balance.setAccount(account);
        balance.setBalance(balanceAmount);
        balance.setDateTime(dateTime);

        return balance;
    }

    public Account find(WsAccountRef accountRef) {
        if (accountRef == null) {
            return null;
        }
        Long id = accountRef.getId();
        return accountService.findAccountById(id);
    }

}
