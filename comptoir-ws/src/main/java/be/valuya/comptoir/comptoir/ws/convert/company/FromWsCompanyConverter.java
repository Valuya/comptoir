package be.valuya.comptoir.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.CompanyService;
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
        Long id = wsCompany.getId();
        List<WsLocaleText> description = wsCompany.getDescription();
        List<WsLocaleText> name = wsCompany.getName();

        LocaleText wsName = fromWsLocaleTextConverter.convert(name);
        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);

        Company company = new Company();
        company.setId(id);
        company.setDescription(wsDescription);
        company.setName(wsName);

        return company;
    }
    
    public Company find(WsCompanyRef wsCompanyRef) {
        Long id = wsCompanyRef.getId();
        return companyService.findCompanyById(id);
    }

}
