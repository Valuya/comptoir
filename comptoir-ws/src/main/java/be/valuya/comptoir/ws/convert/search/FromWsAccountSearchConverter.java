package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsAccountSearch;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public AccountSearch convert(WsAccountSearch wsAccountSearch) {
        AccountType accountType = wsAccountSearch.getAccountType();
        WsCompanyRef companyRef = wsAccountSearch.getCompanyRef();

        Company company = fromWsCompanyConverter.find(companyRef);

        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setAccountType(accountType);
        accountSearch.setCompany(company);

        return accountSearch;
    }

}
