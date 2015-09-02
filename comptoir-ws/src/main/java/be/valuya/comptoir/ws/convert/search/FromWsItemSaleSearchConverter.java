package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemSaleSearch;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSaleSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
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
    private FromWsItemVariantConverter fromWsItemConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;

    public ItemSaleSearch convert(WsItemSaleSearch wsItemSaleSearch) {
        if (wsItemSaleSearch == null) {
            return null;
        }

        WsCompanyRef companyRef = wsItemSaleSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsItemVariantRef itemVariantRef = wsItemSaleSearch.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemConverter.find(itemVariantRef);

        WsSaleRef saleRef = wsItemSaleSearch.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        ItemSaleSearch itemSaleSearch = new ItemSaleSearch();
        itemSaleSearch.setCompany(company);
        itemSaleSearch.setSale(sale);
        itemSaleSearch.setItemVariant(itemVariant);

        return itemSaleSearch;
    }

}
