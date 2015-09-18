package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsLocaleSearch;
import be.valuya.comptoir.api.domain.search.WsPosSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.LocaleSearch;
import be.valuya.comptoir.model.search.PosSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsPosSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
 @Inject
    private FromWsLocaleSearchConverter fromWsLocaleSearchConverter;

    public PosSearch convert(WsPosSearch wsPosSearch) {
        if (wsPosSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsPosSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);
        
        WsLocaleSearch wsLocaleSearch = wsPosSearch.getLocaleSearch();
        LocaleSearch localeSearch = fromWsLocaleSearchConverter.convert(wsLocaleSearch);

        PosSearch posSearch = new PosSearch();
        posSearch.setCompany(company);
        posSearch.setLocaleSearch(localeSearch);

        return posSearch;
    }

}
