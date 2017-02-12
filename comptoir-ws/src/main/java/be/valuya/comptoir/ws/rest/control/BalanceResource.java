package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.accounting.WsBalance;
import be.valuya.comptoir.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.api.domain.search.WsBalanceSearch;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.search.BalanceSearch;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.util.pagination.BalanceColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.accounting.FromWsBalanceConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsBalanceConverter;
import be.valuya.comptoir.ws.convert.search.FromWsBalanceSearchConverter;
import be.valuya.comptoir.ws.rest.validation.BalanceStateChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/balance")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({Roles.EMPLOYEE})
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
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;


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
        Pagination<Balance, BalanceColumn> pagination = restPaginationUtil.extractPagination(uriInfo, BalanceColumn::valueOf);
        BalanceSearch balanceSearch = fromWsBalanceSearchConverter.convert(wsBalanceSearch);
        List<Balance> balances = accountService.findBalances(balanceSearch, pagination);

        List<WsBalance> wsBalances = balances.stream()
                .map(toWsBalanceConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

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
