package be.valuya.comptoir.ws.convert.cash;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPileRef;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsBalanceConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 *
 */
@ApplicationScoped
public class ToWsMoneyPileConverter {

    @Inject
    private ToWsAccountConverter toWsAccountConverter;
    @Inject
    private ToWsBalanceConverter toWsBalanceConverter;

    public WsMoneyPile convert(MoneyPile moneyPile) {
        if (moneyPile == null) {
            return null;
        }
        Long id = moneyPile.getId();
        ZonedDateTime dateTime = moneyPile.getDateTime();
        BigDecimal count = moneyPile.getCount();
        BigDecimal unitAmount = moneyPile.getUnitAmount();
        BigDecimal total = moneyPile.getTotal();

        Account account = moneyPile.getAccount();
        WsAccountRef accountRef = toWsAccountConverter.reference(account);

        Balance balance = moneyPile.getBalance();
        WsBalanceRef balanceRef = toWsBalanceConverter.reference(balance);

        WsMoneyPile wsMoneyPile = new WsMoneyPile();
        wsMoneyPile.setId(id);
        wsMoneyPile.setAccountRef(accountRef);
        wsMoneyPile.setBalanceRef(balanceRef);
        wsMoneyPile.setDateTime(dateTime);
        wsMoneyPile.setCount(count);
        wsMoneyPile.setTotal(total);
        wsMoneyPile.setUnitAmount(unitAmount);

        return wsMoneyPile;
    }

    public WsMoneyPileRef reference(MoneyPile moneyPile) {
        Long id = moneyPile.getId();
        WsMoneyPileRef wsMoneyPileRef = new WsMoneyPileRef(id);
        return wsMoneyPileRef;
    }

}
