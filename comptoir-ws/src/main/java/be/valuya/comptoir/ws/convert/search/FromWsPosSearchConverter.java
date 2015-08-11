package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsPosSearch;
import be.valuya.comptoir.model.company.Company;
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

    public PosSearch convert(WsPosSearch wsPosSearch) {
        WsCompanyRef companyRef = wsPosSearch.getCompanyRef();

        Company company = fromWsCompanyConverter.find(companyRef);

        PosSearch posSearch = new PosSearch();
        posSearch.setCompany(company);

        return posSearch;
    }

}
