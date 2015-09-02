package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.api.domain.commercial.WsAttributeValue;
import be.valuya.comptoir.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.StockService;
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
public class FromWsAttributeValueConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsAttributeDefinitionConverter fromWsAttributeDefinitionConverter;

    public AttributeValue convert(WsAttributeValue wsAttributeValue) {
        if (wsAttributeValue == null) {
            return null;
        }
        Long id = wsAttributeValue.getId();

        List<WsLocaleText> value = wsAttributeValue.getValue();
        LocaleText wsValue = fromWsLocaleTextConverter.convert(value);

        WsAttributeDefinitionRef attributeDefinitionRef = wsAttributeValue.getAttributeDefinitionRef();
        AttributeDefinition attributeDefinition = fromWsAttributeDefinitionConverter.find(attributeDefinitionRef);

        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(id);
        attributeValue.setValue(wsValue);
        attributeValue.setAttributeDefinition(attributeDefinition);

        return attributeValue;
    }

    public AttributeValue find(WsAttributeValueRef attributeValueRef) {
        if (attributeValueRef == null) {
            return null;
        }
        Long attributeValueId = attributeValueRef.getId();
        AttributeValue attributeValueitem = stockService.findAttributeValueById(attributeValueId);
        return attributeValueitem;
    }

}
