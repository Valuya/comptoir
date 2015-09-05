package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.api.domain.search.WsItemVariantSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemVariantSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemVariantSearchConverter {

    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @Inject
    private FromWsItemSearchConverter fromWsItemSearchConverter;

    public ItemVariantSearch convert(WsItemVariantSearch wsItemVariantSearch) {
        if (wsItemVariantSearch == null) {
            return null;
        }
        WsItemRef wsItemRef = wsItemVariantSearch.getItemRef();
        WsItemSearch wsItemSearch = wsItemVariantSearch.getItemSearch();
        String variantReference = wsItemVariantSearch.getVariantReference();
        String variantReferenceContains = wsItemVariantSearch.getVariantReferenceContains();

        ItemSearch itemSearch = fromWsItemSearchConverter.convert(wsItemSearch);
        Item item = fromWsItemConverter.find(wsItemRef);

        ItemVariantSearch itemVariantSearch = new ItemVariantSearch();
        itemVariantSearch.setItem(item);
        itemVariantSearch.setItemSearch(itemSearch);
        itemVariantSearch.setVariantReference(variantReference);
        itemVariantSearch.setVariantReferenceContains(variantReferenceContains);

        return itemVariantSearch;
    }

}
