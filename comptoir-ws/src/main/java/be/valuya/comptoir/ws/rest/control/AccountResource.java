package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccount;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAccountSearch;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.util.pagination.AccountColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.rest.api.AccountResourceApi;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAccountSearchConverter;
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
public class AccountResource implements AccountResourceApi {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsAccountConverter fromWsAccountConverter;
    @Inject
    private FromWsAccountSearchConverter fromWsAccountSearchConverter;
    @Inject
    private ToWsAccountConverter toWsAccountConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private EmployeeAccessChecker employeeAccessChecker;

    public WsAccountRef createAccount(WsAccount wsAccount) {
        Account account = fromWsAccountConverter.convert(wsAccount);
        employeeAccessChecker.checkOwnCompany(account);
        Account savedAccount = accountService.saveAccount(account);

        WsAccountRef accountRef = toWsAccountConverter.reference(savedAccount);

        return accountRef;
    }

    public WsAccountRef updateAccount(long id, WsAccount wsAccount) {
        idChecker.checkId(id, wsAccount);
        Account account = fromWsAccountConverter.convert(wsAccount);
        employeeAccessChecker.checkOwnCompany(account);
        Account savedAccount = accountService.saveAccount(account);

        WsAccountRef accountRef = toWsAccountConverter.reference(savedAccount);

        return accountRef;
    }

    public WsAccount getAccount(long id) {
        Account account = accountService.findAccountById(id);
        employeeAccessChecker.checkOwnCompany(account);

        WsAccount wsAccount = toWsAccountConverter.convert(account);

        return wsAccount;
    }

    public WsAccountSearchResult findAccounts(WsAccountSearch wsAccountSearch) {
        Pagination<Account, AccountColumn> pagination = restPaginationUtil.extractPagination(uriInfo, AccountColumn::valueOf);

        AccountSearch accountSearch = fromWsAccountSearchConverter.convert(wsAccountSearch);
        employeeAccessChecker.checkOwnCompany(accountSearch);
        List<Account> accounts = accountService.findAccounts(accountSearch, pagination);

        List<WsAccountRef> wsAccounts = accounts.stream()
                .map(toWsAccountConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return restPaginationUtil.setResults(new WsAccountSearchResult(), wsAccounts, pagination);
    }

}
