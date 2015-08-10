package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsAccountSearch;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountSearchConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsAccountSearch convert(AccountSearch accountSearch) {
        AccountType accountType = accountSearch.getAccountType();
        Company company = accountSearch.getCompany();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsAccountSearch wsAccountSearch = new WsAccountSearch();
        wsAccountSearch.setAccountType(accountType);
        wsAccountSearch.setCompanyRef(companyRef);

        return wsAccountSearch;
    }

}
