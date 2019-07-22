package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsItemVariantConverter {

    @Inject
    private ToWsAttributeValueConverter toWsAttributeValueConverter;
    @Inject
    private ToWsPictureConverter toWsPictureConverter;
    @Inject
    private ToWsItemConverter toWsItemConverter;
    @Inject
    private ToWsPricingConverter toWsPricingConverter;

    public WsItemVariant convert(ItemVariant itemVariant) {
        if (itemVariant == null) {
            return null;
        }
        Long id = itemVariant.getId();
        String variantReference = itemVariant.getVariantReference();

        Picture mainPicture = itemVariant.getMainPicture();
        WsPictureRef mainPictureRef = toWsPictureConverter.reference(mainPicture);

        Pricing pricing = itemVariant.getPricing();
        WsPricing wsPricing = toWsPricingConverter.toWsPricing(pricing);
        BigDecimal pricingAmount = itemVariant.getPricingAmount();

        // Copy to List : IndirectList does not handle stream
        List<AttributeValue> attributeValues = new ArrayList<>(itemVariant.getAttributeValues());
        List<WsAttributeValueRef> attributeValueRefs = attributeValues
                .stream()
                .map(toWsAttributeValueConverter::reference)
                .collect(Collectors.toList());

        Item item = itemVariant.getItem();
        WsItemRef itemRef = toWsItemConverter.reference(item);

        WsItemVariant wsItem = new WsItemVariant();
        wsItem.setId(id);
        wsItem.setMainPictureRef(mainPictureRef);
        wsItem.setVariantReference(variantReference);
        wsItem.setPricing(wsPricing);
        wsItem.setPricingAmount(pricingAmount);
        wsItem.setAttributeValueRefs(attributeValueRefs);
        wsItem.setItemRef(itemRef);
        return wsItem;
    }

    public WsItemVariantRef reference(ItemVariant itemVariant) {
        if (itemVariant == null) {
            return null;
        }
        Long id = itemVariant.getId();
        WsItemVariantRef itemVariantRef = new WsItemVariantRef(id);
        return itemVariantRef;
    }

}
