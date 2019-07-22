package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.ws.convert.accounting.FromWsAccountTypeConverter;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountType;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAccountSearch;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsPosConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsPosConverter fromWsPosConverter;
    @Inject
    private FromWsAccountTypeConverter fromWsAccountTypeConverter;

    public AccountSearch convert(WsAccountSearch wsAccountSearch) {
        if (wsAccountSearch == null) {
            return null;
        }
        WsAccountType wsAccountType = wsAccountSearch.getAccountType();
        Optional<AccountType> accountTypeOptional = Optional.ofNullable(wsAccountType)
                .map(fromWsAccountTypeConverter::fromWsAccountType);
        Boolean cash = wsAccountSearch.getCash();

        WsCompanyRef companyRef = wsAccountSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsPosRef posRef = wsAccountSearch.getPosRef();
        Optional<Pos> posOptional = Optional.ofNullable(posRef)
                .map(fromWsPosConverter::find);

        String multiSearch = wsAccountSearch.getMultiSearch();

        AccountSearch accountSearch = new AccountSearch();
        accountTypeOptional.ifPresent(accountSearch::setAccountType);
        accountSearch.setCompany(company);
        posOptional.ifPresent(accountSearch::setPos);
        accountSearch.setCash(cash);
        accountSearch.setMultiSearchOptional(Optional.ofNullable(multiSearch));

        return accountSearch;
    }

}
