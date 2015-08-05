package be.valuya.comptoir.comptoir.ws;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class WsCompanyConverter {

    @Inject
    private WsLocaleTestConverter wsLocaleTestConverter;

    public WsCompany convertCompany(Company company) {
        Long id = company.getId();
        LocaleText description = company.getDescription();
        LocaleText name = company.getName();

        WsLocaleText wsName = wsLocaleTestConverter.convertLocaleText(name);
        WsLocaleText wsDescription = wsLocaleTestConverter.convertLocaleText(description);

        WsCompany wsCompany = new WsCompany();
        wsCompany.setId(id);
        wsCompany.setDescription(wsDescription);
        wsCompany.setName(wsName);

        return wsCompany;
    }

    public Company convertWsCompany(WsCompany wsCompany) {
        Long id = wsCompany.getId();
        WsLocaleText description = wsCompany.getDescription();
        WsLocaleText name = wsCompany.getName();

        LocaleText wsName = wsLocaleTestConverter.convertWsLocaleText(name);
        LocaleText wsDescription = wsLocaleTestConverter.convertWsLocaleText(description);

        Company company = new Company();
        company.setId(id);
        company.setDescription(wsDescription);
        company.setName(wsName);

        return company;
    }

}
