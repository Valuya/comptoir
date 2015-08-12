package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsCompanyConverter {

    @Inject
    private ToWsLocaleTextConverter toWsLocaleTextConverter;

    public WsCompany convert(Company company) {
        if (company == null) {
            return null;
        }
        Long id = company.getId();
        LocaleText description = company.getDescription();
        LocaleText name = company.getName();

        List<WsLocaleText> wsName = toWsLocaleTextConverter.convert(name);
        List<WsLocaleText> wsDescription = toWsLocaleTextConverter.convert(description);

        WsCompany wsCompany = new WsCompany();
        wsCompany.setId(id);
        wsCompany.setDescription(wsDescription);
        wsCompany.setName(wsName);

        return wsCompany;
    }

    public WsCompanyRef reference(Company company) {
        Long id = company.getId();
        WsCompanyRef wsCompanyRef = new WsCompanyRef(id);
        return wsCompanyRef;
    }

}
