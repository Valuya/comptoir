package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.CompanyService;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsCompanyConverter {

    @EJB
    private CompanyService companyService;
    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;

    public Company convert(WsCompany wsCompany) {
        Company company = new Company();

        return updateCompany(wsCompany, company);
    }

    public Company updateCompany(WsCompany wsCompany, Company company) {
        Long id = wsCompany.getId();

        List<WsLocaleText> newName = wsCompany.getName();
        LocaleText existingName = company.getName();
        LocaleText name = fromWsLocaleTextConverter.update(newName, existingName);

        List<WsLocaleText> newDescription = wsCompany.getDescription();
        LocaleText existingDescription = company.getDescription();
        LocaleText description = fromWsLocaleTextConverter.update(newDescription, existingDescription);

        company.setId(id);
        company.setDescription(description);
        company.setName(name);

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
