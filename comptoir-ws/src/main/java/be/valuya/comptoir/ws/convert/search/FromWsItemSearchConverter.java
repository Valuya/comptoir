package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import java.util.Locale;
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
        Locale locale = wsItemSearch.getLocale();
        Boolean multipleSale = wsItemSearch.getMultipleSale();

        Company company = fromWsCompanyConverter.find(companyRef);

        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCompany(company);
        itemSearch.setDescriptionContains(descriptionContains);
        itemSearch.setMultiSearch(multiSearch);
        itemSearch.setNameContains(nameContains);
        itemSearch.setReference(reference);
        itemSearch.setReferenceContains(referenceContains);
        itemSearch.setLocale(locale);
        itemSearch.setMultipleSale(multipleSale);

        return itemSearch;
    }

}
