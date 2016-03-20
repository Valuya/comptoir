package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsStockSearch;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.StockSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsPosConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsStockSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsPosConverter fromWsPosConverter;

    public StockSearch convert(WsStockSearch wsStockSearch) {
        if (wsStockSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsStockSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);
        Boolean active = wsStockSearch.getActive();
        WsPosRef posRef = wsStockSearch.getPosRef();
        Pos pos = fromWsPosConverter.find(posRef);


        StockSearch stockSearch = new StockSearch();
        stockSearch.setActive(active);
        stockSearch.setCompany(company);
        stockSearch.setPos(pos);
        return stockSearch;
    }

}
