package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.model.commercial.*;
import be.valuya.comptoir.service.StockService;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemVariantConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsAttributeValueConverter fromWsAttributeValueConverter;
    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @Inject
    private FromWsPictureConverter fromWsPictureConverter;

    public ItemVariant convert(WsItemVariant wsItemVariant) {
        if (wsItemVariant == null) {
            return null;
        }
        Long id = wsItemVariant.getId();

        String variantReference = wsItemVariant.getVariantReference();
        Pricing pricing = wsItemVariant.getPricing();
        BigDecimal pricingAmount = wsItemVariant.getPricingAmount();

        WsPictureRef mainPictureRef = wsItemVariant.getMainPictureRef();

        Picture mainPicture = fromWsPictureConverter.find(mainPictureRef);

        WsItemRef itemRef = wsItemVariant.getItemRef();
        Item item = fromWsItemConverter.find(itemRef);

        List<AttributeValue> attributeValues = wsItemVariant.getAttributeValueRefs()
                .stream()
                .map(fromWsAttributeValueConverter::find)
                .collect(Collectors.toList());

        ItemVariant itemVariant = new ItemVariant();
        itemVariant.setId(id);
        itemVariant.setMainPicture(mainPicture);
        itemVariant.setVariantReference(variantReference);
        itemVariant.setPricing(pricing);
        itemVariant.setPricingAmount(pricingAmount);
        itemVariant.setItem(item);
        itemVariant.setAttributeValues(attributeValues);
        itemVariant.setActive(true);

        return itemVariant;
    }

    public ItemVariant find(WsItemVariantRef itemVariantRef) {
        if (itemVariantRef == null) {
            return null;
        }
        Long itemVariantId = itemVariantRef.getId();
        ItemVariant itemVariant = stockService.findItemVariantById(itemVariantId);
        return itemVariant;
    }

}
