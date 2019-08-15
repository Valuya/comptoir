package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariantSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class ToWsItemVariantSalePriceDetailsConverter {

    @Inject
    private ToWsItemVariantSaleConverter toWsItemVariantSaleConverter;

    public WsItemVariantSalePriceDetails convert(ItemVariantSalePriceDetails priceDetails, ItemVariantSale variantSale) {
        WsItemVariantSaleRef variantSaleRef = toWsItemVariantSaleConverter.reference(variantSale);
        int quantity = priceDetails.getQuantity();
        BigDecimal unitPriceVatExclusive = priceDetails.getUnitPriceVatExclusive();
        BigDecimal totalVatExclusivePriorDiscount = priceDetails.getTotalVatExclusivePriorDiscount();
        BigDecimal discountRatio = priceDetails.getDiscountRatio();
        BigDecimal effectiveDiscountRatio = priceDetails.getEffectiveDiscountRatio();
        BigDecimal discountAmount = priceDetails.getDiscountAmount();
        BigDecimal totalVatExclusive = priceDetails.getTotalVatExclusive();
        BigDecimal vatRate = priceDetails.getVatRate();
        BigDecimal vatAmount = priceDetails.getVatAmount();
        BigDecimal totalVatInclusive = priceDetails.getTotalVatInclusive();

        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = new WsItemVariantSalePriceDetails();
        wsItemVariantSalePriceDetails.setVariantSaleRef(variantSaleRef);
        wsItemVariantSalePriceDetails.setQuantity(quantity);
        wsItemVariantSalePriceDetails.setUnitPriceVatExclusive(unitPriceVatExclusive);
        wsItemVariantSalePriceDetails.setTotalVatExclusivePriorDiscount(totalVatExclusivePriorDiscount);
        wsItemVariantSalePriceDetails.setDiscountRatio(discountRatio);
        wsItemVariantSalePriceDetails.setEffectiveDiscountRatio(effectiveDiscountRatio);
        wsItemVariantSalePriceDetails.setDiscountAmount(discountAmount);
        wsItemVariantSalePriceDetails.setTotalVatExclusive(totalVatExclusive);
        wsItemVariantSalePriceDetails.setVatRate(vatRate);
        wsItemVariantSalePriceDetails.setVatAmount(vatAmount);
        wsItemVariantSalePriceDetails.setTotalVatInclusive(totalVatInclusive);
        return wsItemVariantSalePriceDetails;
    }
}
