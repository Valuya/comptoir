package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemSaleSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSaleSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.ws.convert.commercial.FromWsSaleConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemSaleSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;

    public ItemSaleSearch convert(WsItemSaleSearch wsItemSaleSearch) {
        if (wsItemSaleSearch == null) {
            return null;
        }

        WsCompanyRef companyRef = wsItemSaleSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsItemRef itemRef = wsItemSaleSearch.getItemRef();
        Item item = fromWsItemConverter.find(itemRef);

        WsSaleRef saleRef = wsItemSaleSearch.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        ItemSaleSearch itemSaleSearch = new ItemSaleSearch();
        itemSaleSearch.setCompany(company);
        itemSaleSearch.setSale(sale);
        itemSaleSearch.setItem(item);

        return itemSaleSearch;
    }

}
