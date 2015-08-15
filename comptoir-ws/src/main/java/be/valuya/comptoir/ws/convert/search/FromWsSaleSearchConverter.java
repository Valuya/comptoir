package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsSaleSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public SaleSearch convert(WsSaleSearch wsSaleSearch) {
        if (wsSaleSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsSaleSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);
        Boolean closed = wsSaleSearch.getClosed();

        SaleSearch saleSearch = new SaleSearch();
        saleSearch.setCompany(company);
        saleSearch.setClosed(closed);

        return saleSearch;
    }

}
