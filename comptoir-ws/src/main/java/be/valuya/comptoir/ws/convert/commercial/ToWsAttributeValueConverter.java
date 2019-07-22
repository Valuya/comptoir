package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValue;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeValue;
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
public class ToWsAttributeValueConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsAttributeDefinitionConverter toWsAttributeDefinitionConverter;

    public WsAttributeValue convert(AttributeValue attributeValue) {
        if (attributeValue == null) {
            return null;
        }
        Long id = attributeValue.getId();
        AttributeDefinition attributeDefinition = attributeValue.getAttributeDefinition();
        LocaleText value = attributeValue.getValue();

        WsAttributeDefinitionRef attributeDefinitionRef = toWsAttributeDefinitionConverter.reference(attributeDefinition);
        List<WsLocaleText> wsValue = fromWsLocaleTextConverter.convert(value);

        WsAttributeValue wsAttributeValue = new WsAttributeValue();
        wsAttributeValue.setId(id);
        wsAttributeValue.setAttributeDefinitionRef(attributeDefinitionRef);
        wsAttributeValue.setValue(wsValue);

        return wsAttributeValue;
    }

    public WsAttributeValueRef reference(AttributeValue attributeValue) {
        if (attributeValue == null) {
            return null;
        }
        Long id = attributeValue.getId();
        WsAttributeValueRef attributeValueRef = new WsAttributeValueRef(id);
        return attributeValueRef;
    }

}
