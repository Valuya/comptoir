package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntrySearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAccountingEntrySearch;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.rest.api.AccountingEntryResourceApi;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAccountingEntrySearchConverter;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class AccountingEntryResource implements AccountingEntryResourceApi {

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
    @Inject
    private EmployeeAccessChecker employeeAccessChecker;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;

    public WsAccountingEntryRef createAccountingEntry(WsAccountingEntry wsAccountingEntry) {
        AccountingEntry accountingEntry = fromWsAccountingEntryConverter.convert(wsAccountingEntry);
        employeeAccessChecker.checkOwnCompany(accountingEntry);
        AccountingEntry savedAccountingEntry = accountingEntryService.saveAccountingEntry(accountingEntry);

        WsAccountingEntryRef accountingEntryRef = toWsAccountingEntryConverter.reference(savedAccountingEntry);

        return accountingEntryRef;
    }

    public WsAccountingEntryRef updateAccountingEntry(long id, WsAccountingEntry wsAccountingEntry) {
        idChecker.checkId(id, wsAccountingEntry);
        AccountingEntry accountingEntry = fromWsAccountingEntryConverter.convert(wsAccountingEntry);
        employeeAccessChecker.checkOwnCompany(accountingEntry);
        AccountingEntry savedAccountingEntry = accountingEntryService.saveAccountingEntry(accountingEntry);

        WsAccountingEntryRef accountingEntryRef = toWsAccountingEntryConverter.reference(savedAccountingEntry);

        return accountingEntryRef;
    }

    public WsAccountingEntry getAccountingEntry(long id) {
        AccountingEntry accountingEntry = accountingEntryService.findAccountingEntryById(id);
        employeeAccessChecker.checkOwnCompany(accountingEntry);

        WsAccountingEntry wsAccountingEntry = toWsAccountingEntryConverter.convert(accountingEntry);

        return wsAccountingEntry;
    }

    public void deleteAccountingEntry(long id) {
        AccountingEntry accountingEntry = accountingEntryService.findAccountingEntryById(id);
        employeeAccessChecker.checkOwnCompany(accountingEntry);
        accountingEntryService.removeAccountingEntry(accountingEntry);
    }

    public WsAccountingEntrySearchResult findAccountingEntries(WsAccountingEntrySearch wsAccountingEntrySearch) {
        Pagination<Object, ?> pagination = restPaginationUtil.extractPagination(uriInfo);
        AccountingEntrySearch accountingEntrySearch = fromWsAccountingEntrySearchConverter.convert(wsAccountingEntrySearch);
        employeeAccessChecker.checkOwnCompany(accountingEntrySearch);
        List<AccountingEntry> accountingEntrys = accountingEntryService.findAccountingEntries(accountingEntrySearch);

        List<WsAccountingEntryRef> wsAccountingEntrys = accountingEntrys.stream()
                .map(toWsAccountingEntryConverter::reference)
                .collect(Collectors.toList());

        response.setHeader(ApiParameters.LIST_RESULTS_COUNT_HEADER, "101");

        return restPaginationUtil.setResults(new WsAccountingEntrySearchResult(), wsAccountingEntrys, pagination);
    }

}
