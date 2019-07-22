package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPos;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.service.PosService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsCustomerConverter;
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
    @Inject
    private FromWsCustomerConverter fromWsCustomerConverter;
    @EJB
    private PosService posService;

    public Pos convert(WsPos wsPos) {
        if (wsPos == null) {
            return null;
        }
        Long id = wsPos.getId();
        WsCompanyRef companyRef = wsPos.getCompanyRef();
        List<WsLocaleText> description = wsPos.getDescription();
        String name = wsPos.getName();

        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);
        Company company = fromWsCompanyConverter.find(companyRef);
        
        WsCustomerRef defaultCustomerRef = wsPos.getDefaultCustomerRef();
        Customer customer = fromWsCustomerConverter.find(defaultCustomerRef);

        Pos pos = new Pos();
        pos.setId(id);
        pos.setDescription(wsDescription);
        pos.setCompany(company);
        pos.setName(name);
        pos.setDefaultCustomer(customer);

        return pos;
    }

    public Pos find(WsPosRef posRef) {
        if (posRef == null) {
            return null;
        }
        Long id = posRef.getId();
        return posService.findPosById(id);
    }

}
