package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.api.domain.search.WsLocaleSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.search.LocaleSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsLocaleSearchConverter fromWsLocaleSearchConverter;

    public ItemSearch convert(WsItemSearch wsItemSearch) {
        if (wsItemSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsItemSearch.getCompanyRef();
        String descriptionContains = wsItemSearch.getDescriptionContains();
        String multiSearch = wsItemSearch.getMultiSearch();
        String nameContains = wsItemSearch.getNameContains();
        String reference = wsItemSearch.getReference();
        String referenceContains = wsItemSearch.getReferenceContains();

        Company company = fromWsCompanyConverter.find(companyRef);

        WsLocaleSearch wsLocaleSearch = wsItemSearch.getLocaleSearch();
        LocaleSearch localeSearch = fromWsLocaleSearchConverter.convert(wsLocaleSearch);

        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCompany(company);
        itemSearch.setDescriptionContains(descriptionContains);
        itemSearch.setMultiSearch(multiSearch);
        itemSearch.setNameContains(nameContains);
        itemSearch.setReference(reference);
        itemSearch.setReferenceContains(referenceContains);
        itemSearch.setLocaleSearch(localeSearch);

        return itemSearch;
    }

}
