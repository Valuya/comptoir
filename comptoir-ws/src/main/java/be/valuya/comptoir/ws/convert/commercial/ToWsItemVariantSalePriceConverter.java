package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariantSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSalePrice;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class ToWsItemVariantSalePriceConverter {

    @Inject
    private ToWsItemVariantSaleConverter toWsItemVariantSaleConverter;

    public WsItemVariantSalePrice convert(ItemVariantSalePriceDetails priceDetails, ItemVariantSale variantSale) {
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

        WsItemVariantSalePrice wsItemVariantSalePrice = new WsItemVariantSalePrice();
        wsItemVariantSalePrice.setVariantSaleRef(variantSaleRef);
        wsItemVariantSalePrice.setQuantity(quantity);
        wsItemVariantSalePrice.setUnitPriceVatExclusive(unitPriceVatExclusive);
        wsItemVariantSalePrice.setTotalVatExclusivePriorDiscount(totalVatExclusivePriorDiscount);
        wsItemVariantSalePrice.setDiscountRatio(discountRatio);
        wsItemVariantSalePrice.setEffectiveDiscountRatio(effectiveDiscountRatio);
        wsItemVariantSalePrice.setDiscountAmount(discountAmount);
        wsItemVariantSalePrice.setTotalVatExclusive(totalVatExclusive);
        wsItemVariantSalePrice.setVatRate(vatRate);
        wsItemVariantSalePrice.setVatAmount(vatAmount);
        wsItemVariantSalePrice.setTotalVatInclusive(totalVatInclusive);
        return wsItemVariantSalePrice;
    }
}
