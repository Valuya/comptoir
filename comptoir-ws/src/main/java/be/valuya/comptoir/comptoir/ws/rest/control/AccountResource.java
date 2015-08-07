package be.valuya.comptoir.comptoir.ws.rest.control;

import be.valuya.comptoir.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.api.domain.accounting.WsAccount;
import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.comptoir.ws.convert.accounting.FromWsAccountConverter;
import be.valuya.comptoir.comptoir.ws.convert.accounting.ToWsAccountConverter;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.service.AccountService;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/account")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AccountResource {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsAccountConverter fromWsAccountConverter;
    @Inject
    private ToWsAccountConverter toWsAccountConverter;
    @Inject
    private IdChecker idChecker;

    @POST
    public WsAccountRef createAccount(@NoId WsAccount wsAccount) {
        return saveAccount(wsAccount);
    }

    @Path("{id}")
    @PUT
    public WsAccountRef saveAccount(@PathParam("id") long id, WsAccount wsAccount) {
        idChecker.checkId(id, wsAccount);
        return saveAccount(wsAccount);
    }

    @Path("{id}")
    @GET
    public WsAccount getAccount(@PathParam("id") long id) {
        Account account = accountService.findAccountById(id);

        WsAccount wsAccount = toWsAccountConverter.convert(account);

        return wsAccount;
    }

    @POST
    @Path("search")
    public List<WsAccount> findAccounts(AccountSearch accountSearch) {
        List<Account> accounts = accountService.findAccounts(accountSearch);

        List<WsAccount> wsAccounts = accounts.stream()
                .map(toWsAccountConverter::convert)
                .collect(Collectors.toList());

        return wsAccounts;
    }

    private WsAccountRef saveAccount(WsAccount wsAccount) {
        Account account = fromWsAccountConverter.convert(wsAccount);
        Account savedAccount = accountService.saveAccount(account);

        WsAccountRef accountRef = toWsAccountConverter.reference(savedAccount);

        return accountRef;
    }

}
