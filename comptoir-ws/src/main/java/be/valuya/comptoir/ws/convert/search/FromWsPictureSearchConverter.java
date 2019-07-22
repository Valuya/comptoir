package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.search.WsPictureSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.PictureSearch;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsPictureSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @Inject
    private FromWsItemVariantConverter fromWsItemVariantConverter;

    public PictureSearch convert(WsPictureSearch wsPictureSearch) {
        if (wsPictureSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsPictureSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);
        
        WsItemRef itemRef = wsPictureSearch.getItemRef();
        Item item = fromWsItemConverter.find(itemRef);
        
        WsItemVariantRef itemVariantRef = wsPictureSearch.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemVariantConverter.find(itemVariantRef);


        PictureSearch pictureSearch = new PictureSearch();
        pictureSearch.setCompany(company);
        pictureSearch.setItem(item);
        pictureSearch.setItemVariant(itemVariant);

        return pictureSearch;
    }

}
