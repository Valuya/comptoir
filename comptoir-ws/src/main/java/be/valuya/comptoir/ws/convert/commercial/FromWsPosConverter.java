package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsPos;
import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.PosService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
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
public class FromWsPosConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @EJB
    private PosService posService;

    public Pos convert(WsPos wsPos) {
        Long id = wsPos.getId();
        WsCompanyRef companyRef = wsPos.getCompanyRef();
        List<WsLocaleText> description = wsPos.getDescription();
        String name = wsPos.getName();

        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);
        Company company = fromWsCompanyConverter.find(companyRef);

        Pos pos = new Pos();
        pos.setId(id);
        pos.setDescription(wsDescription);
        pos.setCompany(company);
        pos.setName(name);

        return pos;
    }

    public Pos find(WsPosRef posRef) {
        Long id = posRef.getId();
        return posService.findPosById(id);
    }

}
