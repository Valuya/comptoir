package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsPos;
import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsPosConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsPos convert(Pos pos) {
        Long id = pos.getId();
        Company company = pos.getCompany();
        LocaleText description = pos.getDescription();
        String name = pos.getName();

        List<WsLocaleText> wsDescription = fromWsLocaleTextConverter.convert(description);
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsPos wsPos = new WsPos();
        wsPos.setId(id);
        wsPos.setDescription(wsDescription);
        wsPos.setCompanyRef(companyRef);
        wsPos.setName(name);

        return wsPos;
    }

    public WsPosRef reference(Pos pos) {
        Long id = pos.getId();
        WsPosRef wsPosRef = new WsPosRef(id);
        return wsPosRef;
    }

}
