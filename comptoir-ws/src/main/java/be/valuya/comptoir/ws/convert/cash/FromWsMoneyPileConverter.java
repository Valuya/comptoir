package be.valuya.comptoir.ws.convert.cash;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountConverter;
import be.valuya.comptoir.ws.convert.accounting.FromWsBalanceConverter;
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
public class FromWsMoneyPileConverter {

    @Inject
    private FromWsAccountConverter fromWsAccountConverter;
    @Inject
    private FromWsBalanceConverter fromWsBalanceConverter;
    @EJB
    private AccountService accountService;

    public MoneyPile convert(WsMoneyPile wsMoneyPile) {
        if (wsMoneyPile == null) {
            return null;
        }
        Long id = wsMoneyPile.getId();
        ZonedDateTime dateTime = wsMoneyPile.getDateTime();
        BigDecimal unitAmount = wsMoneyPile.getUnitAmount();
        BigDecimal count = wsMoneyPile.getCount();
        BigDecimal total = wsMoneyPile.getTotal();

        WsAccountRef accountRef = wsMoneyPile.getAccountRef();
        Account account = fromWsAccountConverter.find(accountRef);

        WsBalanceRef balanceRef = wsMoneyPile.getBalanceRef();
        Balance balance = fromWsBalanceConverter.find(balanceRef);

        MoneyPile moneyPile = new MoneyPile();
        moneyPile.setId(id);
        moneyPile.setAccount(account);
        moneyPile.setBalance(balance);
        moneyPile.setDateTime(dateTime);
        moneyPile.setUnitAmount(unitAmount);
        moneyPile.setCount(count);
        moneyPile.setTotal(total);

        return moneyPile;
    }

    public Account find(WsAccountRef accountRef) {
        if (accountRef == null) {
            return null;
        }
        Long id = accountRef.getId();
        return accountService.findAccountById(id);
    }

}
