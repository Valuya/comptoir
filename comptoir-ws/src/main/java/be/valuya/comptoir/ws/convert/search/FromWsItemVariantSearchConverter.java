package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.api.domain.search.WsItemVariantSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.ItemVariantSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemVariantSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsItemConverter fromWsItemConverter;

    public ItemVariantSearch convert(WsItemVariantSearch wsItemVariantSearch) {
        if (wsItemVariantSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsItemVariantSearch.getCompanyRef();
        String descriptionContains = wsItemVariantSearch.getDescriptionContains();
        WsItemRef itemRef = wsItemVariantSearch.getItemRef();
        String multiSearch = wsItemVariantSearch.getMultiSearch();
        String nameContains = wsItemVariantSearch.getNameContains();
        String reference = wsItemVariantSearch.getReference();
        String referenceContains = wsItemVariantSearch.getReferenceContains();
        String variantReference = wsItemVariantSearch.getVariantReference();
        String variantReferenceContains = wsItemVariantSearch.getVariantReferenceContains();

        Company company = fromWsCompanyConverter.find(companyRef);
        Item item = fromWsItemConverter.find(itemRef);

        ItemVariantSearch itemVariantSearch = new ItemVariantSearch();
        itemVariantSearch.setCompany(company);
        itemVariantSearch.setDescriptionContains(descriptionContains);
        itemVariantSearch.setItem(item);
        itemVariantSearch.setMultiSearch(multiSearch);
        itemVariantSearch.setNameContains(nameContains);
        itemVariantSearch.setReference(reference);
        itemVariantSearch.setReferenceContains(referenceContains);
        itemVariantSearch.setVariantReference(variantReference);
        itemVariantSearch.setVariantReferenceContains(variantReferenceContains);

        return itemVariantSearch;
    }

}
