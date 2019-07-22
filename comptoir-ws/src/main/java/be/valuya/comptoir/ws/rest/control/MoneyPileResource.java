package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPileRef;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.cash.FromWsMoneyPileConverter;
import be.valuya.comptoir.ws.convert.cash.ToWsMoneyPileConverter;
import be.valuya.comptoir.ws.rest.api.MoneyPileResourceApi;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class MoneyPileResource implements MoneyPileResourceApi {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsMoneyPileConverter fromWsMoneyPileConverter;
    @Inject
    private ToWsMoneyPileConverter toWsMoneyPileConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;
    @Inject
    private EmployeeAccessChecker accessChecker;

    public WsMoneyPileRef createMoneyPile(WsMoneyPile wsMoneyPile) {
        MoneyPile moneyPile = fromWsMoneyPileConverter.convert(wsMoneyPile);
        accessChecker.checkOwnCompany(moneyPile.getAccount());
        MoneyPile savedMoneyPile = accountService.saveMoneyPile(moneyPile);
        WsMoneyPileRef moneyPileRef = toWsMoneyPileConverter.reference(savedMoneyPile);
        return moneyPileRef;
    }

    public WsMoneyPileRef updateMoneyPile(long id, WsMoneyPile wsMoneyPile) {
        idChecker.checkId(id, wsMoneyPile);
        MoneyPile moneyPile = fromWsMoneyPileConverter.convert(wsMoneyPile);
        accessChecker.checkOwnCompany(moneyPile.getAccount());
        MoneyPile savedMoneyPile = accountService.saveMoneyPile(moneyPile);
        WsMoneyPileRef moneyPileRef = toWsMoneyPileConverter.reference(savedMoneyPile);
        return moneyPileRef;
    }

    public WsMoneyPile getMoneyPile(long id) {
        MoneyPile moneyPile = accountService.findMoneyPileById(id);
        accessChecker.checkOwnCompany(moneyPile.getAccount());

        WsMoneyPile wsMoneyPile = toWsMoneyPileConverter.convert(moneyPile);

        return wsMoneyPile;
    }

}
