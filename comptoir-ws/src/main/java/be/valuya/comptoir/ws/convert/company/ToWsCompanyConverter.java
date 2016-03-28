package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.company.WsCountryRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsCompanyConverter {

    @Inject
    private ToWsLocaleTextConverter toWsLocaleTextConverter;
    @Inject
    private ToWsCountryConverter toWsCountryConverter;

    public WsCompany convert(Company company) {
        if (company == null) {
            return null;
        }
        Long id = company.getId();
        LocaleText description = company.getDescription();
        LocaleText name = company.getName();

        List<WsLocaleText> wsName = toWsLocaleTextConverter.convert(name);
        List<WsLocaleText> wsDescription = toWsLocaleTextConverter.convert(description);

        Country country = company.getCountry();
        WsCountryRef wsCountryRef = toWsCountryConverter.reference(country);

        BigDecimal customerLoyaltyRate = company.getCustomerLoyaltyRate();

        WsCompany wsCompany = new WsCompany();
        wsCompany.setId(id);
        wsCompany.setDescription(wsDescription);
        wsCompany.setName(wsName);
        wsCompany.setCountryRef(wsCountryRef);
        wsCompany.setCustomerLoyaltyRate(customerLoyaltyRate);

        return wsCompany;
    }

    public WsCompanyRef reference(Company company) {
        Long id = company.getId();
        WsCompanyRef wsCompanyRef = new WsCompanyRef(id);
        return wsCompanyRef;
    }

}
