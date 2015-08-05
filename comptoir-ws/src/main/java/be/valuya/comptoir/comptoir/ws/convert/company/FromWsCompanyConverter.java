package be.valuya.comptoir.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.comptoir.ws.FromWsLocaleTextConverter;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsCompanyConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;

    public Company convert(WsCompany wsCompany) {
        Long id = wsCompany.getId();
        WsLocaleText description = wsCompany.getDescription();
        WsLocaleText name = wsCompany.getName();

        LocaleText wsName = fromWsLocaleTextConverter.convertWsLocaleText(name);
        LocaleText wsDescription = fromWsLocaleTextConverter.convertWsLocaleText(description);

        Company company = new Company();
        company.setId(id);
        company.setDescription(wsDescription);
        company.setName(wsName);

        return company;
    }

}
