package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.api.domain.cash.WsMoneyPileRef;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.cash.FromWsMoneyPileConverter;
import be.valuya.comptoir.ws.convert.cash.ToWsMoneyPileConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.api.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/moneyPile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class MoneyPileResource {

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

    @POST
    @Valid
    public WsMoneyPileRef createMoneyPile(@NoId @Valid WsMoneyPile wsMoneyPile) {
        MoneyPile moneyPile = fromWsMoneyPileConverter.convert(wsMoneyPile);
        accessChecker.checkOwnCompany(moneyPile.getAccount());
        MoneyPile savedMoneyPile = accountService.saveMoneyPile(moneyPile);
        WsMoneyPileRef moneyPileRef = toWsMoneyPileConverter.reference(savedMoneyPile);
        return moneyPileRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsMoneyPileRef saveMoneyPile(@PathParam("id") long id, @Valid WsMoneyPile wsMoneyPile) {
        idChecker.checkId(id, wsMoneyPile);
        MoneyPile moneyPile = fromWsMoneyPileConverter.convert(wsMoneyPile);
        accessChecker.checkOwnCompany(moneyPile.getAccount());
        MoneyPile savedMoneyPile = accountService.saveMoneyPile(moneyPile);
        WsMoneyPileRef moneyPileRef = toWsMoneyPileConverter.reference(savedMoneyPile);
        return moneyPileRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsMoneyPile getMoneyPile(@PathParam("id") long id) {
        MoneyPile moneyPile = accountService.findMoneyPileById(id);
        accessChecker.checkOwnCompany(moneyPile.getAccount());

        WsMoneyPile wsMoneyPile = toWsMoneyPileConverter.convert(moneyPile);

        return wsMoneyPile;
    }

}
