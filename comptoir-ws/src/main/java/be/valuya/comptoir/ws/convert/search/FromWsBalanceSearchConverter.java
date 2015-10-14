package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsAccountSearch;
import be.valuya.comptoir.api.domain.search.WsBalanceSearch;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.model.search.BalanceSearch;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsBalanceSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsAccountSearchConverter fromWsAccountSearchConverter;
    @Inject
    private FromWsAccountConverter fromWsAccountConverter;

    public BalanceSearch convert(WsBalanceSearch wsBalanceSearch) {
        if (wsBalanceSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsBalanceSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsAccountRef accountRef = wsBalanceSearch.getAccountRef();
        Account account = fromWsAccountConverter.find(accountRef);

        WsAccountSearch wsAccountSearch = wsBalanceSearch.getAccountSearch();
        AccountSearch accountSearch = fromWsAccountSearchConverter.convert(wsAccountSearch);

        ZonedDateTime fromDateTime = wsBalanceSearch.getFromDateTime();
        ZonedDateTime toDateTime = wsBalanceSearch.getToDateTime();

        BalanceSearch balanceSearch = new BalanceSearch();
        balanceSearch.setAccountSearch(accountSearch);
        balanceSearch.setCompany(company);
        balanceSearch.setFromDateTime(fromDateTime);
        balanceSearch.setToDateTime(toDateTime);
        balanceSearch.setAccount(account);

        return balanceSearch;
    }

}
