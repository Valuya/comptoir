package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinition;
import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.AttributeDefinition;
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
public class ToWsAttributeDefinitionConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsAttributeDefinition convert(AttributeDefinition attributeDefinition) {
        if (attributeDefinition == null) {
            return null;
        }
        Long id = attributeDefinition.getId();
        Company company = attributeDefinition.getCompany();
        LocaleText name = attributeDefinition.getName();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);
        List<WsLocaleText> wsName = fromWsLocaleTextConverter.convert(name);

        WsAttributeDefinition wsAttributeDefinition = new WsAttributeDefinition();
        wsAttributeDefinition.setId(id);
        wsAttributeDefinition.setCompanyRef(companyRef);
        wsAttributeDefinition.setName(wsName);

        return wsAttributeDefinition;
    }

    public WsAttributeDefinitionRef reference(AttributeDefinition attributeDefinition) {
        if (attributeDefinition == null) {
            return null;
        }
        Long id = attributeDefinition.getId();
        WsAttributeDefinitionRef attributeDefinitionRef = new WsAttributeDefinitionRef(id);
        return attributeDefinitionRef;
    }

}
