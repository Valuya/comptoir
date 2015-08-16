package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.api.domain.cash.WsMoneyPileRef;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.cash.FromWsMoneyPileConverter;
import be.valuya.comptoir.ws.convert.cash.ToWsMoneyPileConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/moneyPile")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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

    @POST
    public WsMoneyPileRef createMoneyPile(@NoId WsMoneyPile wsMoneyPile) {
        return saveMoneyPile(wsMoneyPile);
    }

    @Path("{id}")
    @PUT
    public WsMoneyPileRef saveMoneyPile(@PathParam("id") long id, WsMoneyPile wsMoneyPile) {
        idChecker.checkId(id, wsMoneyPile);
        return saveMoneyPile(wsMoneyPile);
    }

    @Path("{id}")
    @GET
    public WsMoneyPile getMoneyPile(@PathParam("id") long id) {
        MoneyPile moneyPile = accountService.findMoneyPileById(id);

        WsMoneyPile wsMoneyPile = toWsMoneyPileConverter.convert(moneyPile);

        return wsMoneyPile;
    }

    private WsMoneyPileRef saveMoneyPile(WsMoneyPile wsMoneyPile) {
        MoneyPile moneyPile = fromWsMoneyPileConverter.convert(wsMoneyPile);
        MoneyPile savedMoneyPile = accountService.saveMoneyPile(moneyPile);

        WsMoneyPileRef moneyPileRef = toWsMoneyPileConverter.reference(savedMoneyPile);

        return moneyPileRef;
    }

}
