package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinition;
import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.StockService;
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
public class FromWsAttributeDefinitionConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public AttributeDefinition convert(WsAttributeDefinition wsAttributeDefinition) {
        if (wsAttributeDefinition == null) {
            return null;
        }
        Long id = wsAttributeDefinition.getId();

        List<WsLocaleText> name = wsAttributeDefinition.getName();
        LocaleText wsName = fromWsLocaleTextConverter.convert(name);

        WsCompanyRef companyRef = wsAttributeDefinition.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        AttributeDefinition attributeDefinition = new AttributeDefinition();
        attributeDefinition.setId(id);
        attributeDefinition.setCompany(company);
        attributeDefinition.setName(wsName);

        return attributeDefinition;
    }

    public AttributeDefinition find(WsAttributeDefinitionRef attributeDefinitionRef) {
        if (attributeDefinitionRef == null) {
            return null;
        }
        Long attributeDefinitionId = attributeDefinitionRef.getId();
        AttributeDefinition attributeDefinitionitem = stockService.findAttributeDefinitionById(attributeDefinitionId);
        return attributeDefinitionitem;
    }

}
