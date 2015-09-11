package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.api.domain.search.WsAccountingEntrySearch;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.config.HeadersConfig;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAccountingEntrySearchConverter;
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
@Path("/accountingEntry")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AccountingEntryResource {

    @EJB
    private AccountService accountingEntryService;
    @Inject
    private FromWsAccountingEntryConverter fromWsAccountingEntryConverter;
    @Inject
    private FromWsAccountingEntrySearchConverter fromWsAccountingEntrySearchConverter;
    @Inject
    private ToWsAccountingEntryConverter toWsAccountingEntryConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;

    @POST
    @Valid
    public WsAccountingEntryRef createAccountingEntry(@NoId @Valid WsAccountingEntry wsAccountingEntry) {
        AccountingEntry accountingEntry = fromWsAccountingEntryConverter.convert(wsAccountingEntry);
        AccountingEntry savedAccountingEntry = accountingEntryService.saveAccountingEntry(accountingEntry);

        WsAccountingEntryRef accountingEntryRef = toWsAccountingEntryConverter.reference(savedAccountingEntry);

        return accountingEntryRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsAccountingEntryRef saveAccountingEntry(@PathParam("id") long id, @Valid WsAccountingEntry wsAccountingEntry) {
        idChecker.checkId(id, wsAccountingEntry);
        AccountingEntry accountingEntry = fromWsAccountingEntryConverter.convert(wsAccountingEntry);
        AccountingEntry savedAccountingEntry = accountingEntryService.saveAccountingEntry(accountingEntry);

        WsAccountingEntryRef accountingEntryRef = toWsAccountingEntryConverter.reference(savedAccountingEntry);

        return accountingEntryRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsAccountingEntry getAccountingEntry(@PathParam("id") long id) {
        AccountingEntry accountingEntry = accountingEntryService.findAccountingEntryById(id);

        WsAccountingEntry wsAccountingEntry = toWsAccountingEntryConverter.convert(accountingEntry);

        return wsAccountingEntry;
    }
    
    @Path("{id}")
    @DELETE
    public void deleteAccountingEntry(@PathParam("id") long id) {
        AccountingEntry accountingEntry = accountingEntryService.findAccountingEntryById(id);
        accountingEntryService.removeAccountingEntry(accountingEntry);
    }

    @POST
    @Path("search")
    @Valid
    public List<WsAccountingEntry> findAccountingEntrys(@Valid WsAccountingEntrySearch wsAccountingEntrySearch) {
        AccountingEntrySearch accountingEntrySearch = fromWsAccountingEntrySearchConverter.convert(wsAccountingEntrySearch);
        List<AccountingEntry> accountingEntrys = accountingEntryService.findAccountingEntries(accountingEntrySearch);

        List<WsAccountingEntry> wsAccountingEntrys = accountingEntrys.stream()
                .map(toWsAccountingEntryConverter::convert)
                .collect(Collectors.toList());

        response.setHeader(HeadersConfig.LIST_RESULTS_COUNT_HEADER, "101");

        return wsAccountingEntrys;
    }

}
