package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.model.search.BalanceSearch;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.util.pagination.BalanceColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.accounting.FromWsBalanceConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsBalanceConverter;
import be.valuya.comptoir.ws.convert.cash.ToWsMoneyPileConverter;
import be.valuya.comptoir.ws.convert.search.FromWsBalanceSearchConverter;
import be.valuya.comptoir.ws.rest.api.BalanceResourceApi;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalance;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.ws.rest.api.domain.search.WsBalanceSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.ws.rest.validation.BalanceStateChecker;
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
public class BalanceResource implements BalanceResourceApi {

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
    @Inject
    private EmployeeAccessChecker accessChecker;
    @Inject
    private ToWsMoneyPileConverter toWsMoneyPileConverter;


    public WsBalanceRef createBalance(WsBalance wsBalance) {
        Balance balance = fromWsBalanceConverter.convert(wsBalance);
        accessChecker.checkOwnCompany(balance.getAccount());
        Balance savedBalance = accountService.saveBalance(balance);

        WsBalanceRef balanceRef = toWsBalanceConverter.reference(savedBalance);

        return balanceRef;
    }

    public WsBalanceRef updateBalance(long id, WsBalance wsBalance) {
        idChecker.checkId(id, wsBalance);
        Balance balance = fromWsBalanceConverter.convert(wsBalance);
        accessChecker.checkOwnCompany(balance.getAccount());
        Balance savedBalance = accountService.saveBalance(balance);

        WsBalanceRef balanceRef = toWsBalanceConverter.reference(savedBalance);

        return balanceRef;
    }

    public WsBalance getBalance(long id) {
        Balance balance = accountService.findBalanceById(id);
        accessChecker.checkOwnCompany(balance.getAccount());

        WsBalance wsBalance = toWsBalanceConverter.convert(balance);

        return wsBalance;
    }

    public WsBalanceSearchResult findBalances(WsBalanceSearch wsBalanceSearch) {
        Pagination<Balance, BalanceColumn> pagination = restPaginationUtil.extractPagination(uriInfo, BalanceColumn::valueOf);
        BalanceSearch balanceSearch = fromWsBalanceSearchConverter.convert(wsBalanceSearch);
        accessChecker.checkOwnCompany(balanceSearch);
        List<Balance> balances = accountService.findBalances(balanceSearch, pagination);

        List<WsBalanceRef> wsBalances = balances.stream()
                .map(toWsBalanceConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return restPaginationUtil.setResults(new WsBalanceSearchResult(), wsBalances, pagination);
    }

    public void deleteBalance(long id) {
        Balance balance = accountService.findBalanceById(id);
        balanceStateChecker.checkState(balance, false); // TODO: replace with bean validation
        accountService.cancelOpenBalance(balance);
    }

    public WsBalanceRef closeBalance(long id) {
        Balance balance = accountService.findBalanceById(id);
        balanceStateChecker.checkState(balance, false); // TODO: replace with bean validation
        balance = accountService.closeBalance(balance);
        WsBalanceRef balanceRef = toWsBalanceConverter.reference(balance);

        return balanceRef;
    }

    public List<WsMoneyPile> getBalanceMoneyPiles(long id) {
        Balance balance = accountService.findBalanceById(id);
        List<MoneyPile> moneyPiles = accountService.findMoneyPileListForBalance(balance);
        return moneyPiles.stream()
                .map(toWsMoneyPileConverter::convert)
                .collect(Collectors.toList());
    }
}
