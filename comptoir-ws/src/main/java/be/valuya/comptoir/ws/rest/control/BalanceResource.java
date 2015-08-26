package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.accounting.WsBalance;
import be.valuya.comptoir.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.api.domain.search.WsBalanceSearch;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.search.BalanceSearch;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.config.HeadersConfig;
import be.valuya.comptoir.ws.convert.accounting.FromWsBalanceConverter;
import be.valuya.comptoir.ws.convert.balanceing.ToWsBalanceConverter;
import be.valuya.comptoir.ws.convert.search.FromWsBalanceSearchConverter;
import be.valuya.comptoir.ws.rest.validation.BalanceStateChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
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
@Path("/balance")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class BalanceResource {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsBalanceConverter fromWsBalanceConverter;
    @Inject
    private FromWsBalanceSearchConverter fromWsBalanceSearchConverter;
    @Inject
    private ToWsBalanceConverter toWsBalanceConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private BalanceStateChecker balanceStateChecker;
    @Context
    private HttpServletResponse response;

    @POST
    @Valid
    public WsBalanceRef createBalance(@Valid @NoId WsBalance wsBalance) {
        Balance balance = fromWsBalanceConverter.convert(wsBalance);
        Balance savedBalance = accountService.saveBalance(balance);

        WsBalanceRef balanceRef = toWsBalanceConverter.reference(savedBalance);

        return balanceRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsBalanceRef saveBalance(@PathParam("id") long id, @Valid WsBalance wsBalance) {
        idChecker.checkId(id, wsBalance);
        Balance balance = fromWsBalanceConverter.convert(wsBalance);
        Balance savedBalance = accountService.saveBalance(balance);

        WsBalanceRef balanceRef = toWsBalanceConverter.reference(savedBalance);

        return balanceRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsBalance getBalance(@PathParam("id") long id) {
        Balance balance = accountService.findBalanceById(id);

        WsBalance wsBalance = toWsBalanceConverter.convert(balance);

        return wsBalance;
    }

    @POST
    @Valid
    @Path("search")
    public List<WsBalance> findBalances(@Valid WsBalanceSearch wsBalanceSearch) {
        BalanceSearch balanceSearch = fromWsBalanceSearchConverter.convert(wsBalanceSearch);
        List<Balance> balances = accountService.findBalances(balanceSearch);

        List<WsBalance> wsBalances = balances.stream()
                .map(toWsBalanceConverter::convert)
                .collect(Collectors.toList());

        response.setHeader(HeadersConfig.LIST_RESULTS_COUNT_HEADER, "101");
        return wsBalances;
    }

    @DELETE
    @Path("{id}")
    public void deleteBalance(@PathParam("id") long id) {
        Balance balance = accountService.findBalanceById(id);
        balanceStateChecker.checkState(balance, false); // TODO: replace with bean validation
        accountService.cancelOpenBalance(balance);
    }

    @PUT
    @Valid
    @Path("{id}/state/CLOSED")
    public WsBalanceRef closeBalance(@PathParam("id") long id) {
        Balance balance = accountService.findBalanceById(id);
        balanceStateChecker.checkState(balance, false); // TODO: replace with bean validation
        balance = accountService.closeBalance(balance);
        WsBalanceRef balanceRef = toWsBalanceConverter.reference(balance);

        return balanceRef;
    }

}
