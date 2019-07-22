package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantStockSearch;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.stock.FromWsStockConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemVariantStockSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsItemVariantConverter fromWsItemVariantConverter;
    @Inject
    private FromWsStockConverter fromWsStockConverter;

    public ItemStockSearch convert(WsItemVariantStockSearch wsItemVariantStockSearch) {
        if (wsItemVariantStockSearch == null) {
            return null;
        }

        WsCompanyRef companyRef = wsItemVariantStockSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsItemVariantRef itemVariantRef = wsItemVariantStockSearch.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemVariantConverter.find(itemVariantRef);

        WsStockRef wsStockRef = wsItemVariantStockSearch.getStockRef();
        Stock stock = fromWsStockConverter.find(wsStockRef);

        ZonedDateTime atDateTime = wsItemVariantStockSearch.getAtDateTime();

        ItemStockSearch itemStockSearch = new ItemStockSearch();
        itemStockSearch.setItemVariant( itemVariant);
        itemStockSearch.setStock(stock);
        itemStockSearch.setAtDateTime(atDateTime);
        itemStockSearch.setCompany(company);

        return itemStockSearch;
    }

}
