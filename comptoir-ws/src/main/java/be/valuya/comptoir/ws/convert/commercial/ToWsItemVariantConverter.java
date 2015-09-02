package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import java.math.BigDecimal;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsItemVariantConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private ToWsItemPictureConverter toWsItemPictureConverter;

    public WsItemVariant convert(ItemVariant itemVariant) {
        if (itemVariant == null) {
            return null;
        }
        Long id = itemVariant.getId();
        String variantReference = itemVariant.getVariantReference();

        ItemPicture mainPicture = itemVariant.getMainPicture();
        WsItemPictureRef mainPictureRef = toWsItemPictureConverter.reference(mainPicture);

        Pricing pricing = itemVariant.getPricing();
        BigDecimal pricingAmount = itemVariant.getPricingAmount();

        WsItemVariant wsItem = new WsItemVariant();
        wsItem.setId(id);
        wsItem.setMainPictureRef(mainPictureRef);
        wsItem.setVariantReference(variantReference);
        wsItem.setPricing(pricing);
        wsItem.setPricingAmount(pricingAmount);

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
