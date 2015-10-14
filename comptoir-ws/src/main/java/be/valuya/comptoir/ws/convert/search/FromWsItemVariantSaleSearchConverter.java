package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemVariantSaleSearch;
import be.valuya.comptoir.api.domain.search.WsLocaleSearch;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemVariantSaleSearch;
import be.valuya.comptoir.model.search.LocaleSearch;
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
public class FromWsItemVariantSaleSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsItemVariantConverter fromWsItemVariantConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @Inject
    private FromWsLocaleSearchConverter fromWsLocaleSearchConverter;

    public ItemVariantSaleSearch convert(WsItemVariantSaleSearch wsItemSaleSearch) {
        if (wsItemSaleSearch == null) {
            return null;
        }

        WsCompanyRef companyRef = wsItemSaleSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsItemVariantRef itemVariantRef = wsItemSaleSearch.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemVariantConverter.find(itemVariantRef);

        WsSaleRef saleRef = wsItemSaleSearch.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        WsLocaleSearch wsLocaleSearch = wsItemSaleSearch.getLocaleSearch();
        LocaleSearch localeSearch = fromWsLocaleSearchConverter.convert(wsLocaleSearch);

        ItemVariantSaleSearch itemSaleSearch = new ItemVariantSaleSearch();
        itemSaleSearch.setCompany(company);
        itemSaleSearch.setSale(sale);
        itemSaleSearch.setItemVariant(itemVariant);
        itemSaleSearch.setLocaleSearch(localeSearch);

        return itemSaleSearch;
    }

}
