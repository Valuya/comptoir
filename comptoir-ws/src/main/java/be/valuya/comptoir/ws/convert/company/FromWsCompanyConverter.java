package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompany;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCountryRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.CompanyService;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsCompanyConverter {

    @EJB
    private CompanyService companyService;
    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCountryConverter fromWsCountryConverter;

    public Company convert(WsCompany wsCompany) {
        if (wsCompany == null) {
            return null;
        }
        Company company = new Company();

        return patch(company, wsCompany);
    }

    public Company patch(Company company, WsCompany wsCompany) {
        Long id = wsCompany.getId();

        List<WsLocaleText> newName = wsCompany.getName();
        LocaleText existingName = company.getName();
        LocaleText name = fromWsLocaleTextConverter.update(newName, existingName);

        List<WsLocaleText> newDescription = wsCompany.getDescription();
        LocaleText existingDescription = company.getDescription();
        LocaleText description = fromWsLocaleTextConverter.update(newDescription, existingDescription);

        WsCountryRef countryRef = wsCompany.getCountryRef();
        Country country = fromWsCountryConverter.find(countryRef);

        BigDecimal customerLoyaltyRate = wsCompany.getCustomerLoyaltyRate();

        company.setId(id);
        company.setDescription(description);
        company.setName(name);
        company.setCountry(country);
        company.setCustomerLoyaltyRate(customerLoyaltyRate);

        return company;
    }

    public Company find(WsCompanyRef wsCompanyRef) {
        if (wsCompanyRef == null) {
            return null;
        }
        Long id = wsCompanyRef.getId();
        return companyService.findCompanyById(id);
    }

}
