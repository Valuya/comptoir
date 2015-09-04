package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Pricing;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
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

    public WsItemVariant convert(ItemVariant itemVariant) {
        if (itemVariant == null) {
            return null;
        }
        Long id = itemVariant.getId();
        String variantReference = itemVariant.getVariantReference();

        Picture mainPicture = itemVariant.getMainPicture();
        WsPictureRef mainPictureRef = toWsPictureConverter.reference(mainPicture);

        Pricing pricing = itemVariant.getPricing();
        BigDecimal pricingAmount = itemVariant.getPricingAmount();

        List<WsAttributeValueRef> attributeValueRefs = itemVariant.getAttributeValues()
                .stream()
                .map(toWsAttributeValueConverter::reference)
                .collect(Collectors.toList());

        Item item = itemVariant.getItem();
        WsItemRef itemRef = toWsItemConverter.reference(item);

        WsItemVariant wsItem = new WsItemVariant();
        wsItem.setId(id);
        wsItem.setMainPictureRef(mainPictureRef);
        wsItem.setVariantReference(variantReference);
        wsItem.setPricing(pricing);
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
