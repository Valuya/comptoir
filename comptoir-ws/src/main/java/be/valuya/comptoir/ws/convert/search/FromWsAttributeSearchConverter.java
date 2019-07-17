package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAttributeSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AttributeSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAttributeSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public AttributeSearch convert(WsAttributeSearch wsAttributeSearch) {
        if (wsAttributeSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsAttributeSearch.getCompanyRef();
        String multiSearch = wsAttributeSearch.getMultiSearch();
        String nameContains = wsAttributeSearch.getNameContains();
        String valueContains = wsAttributeSearch.getValueContains();
        Locale locale = wsAttributeSearch.getLocale();

        Company company = fromWsCompanyConverter.find(companyRef);

        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.setCompany(company);
        attributeSearch.setMultiSearch(multiSearch);
        attributeSearch.setNameContains(nameContains);
        attributeSearch.setValueContains(valueContains);
        attributeSearch.setLocale(locale);

        return attributeSearch;
    }

}
