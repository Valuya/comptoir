package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class ToWsSalePriceDetailsConverter {

    @Inject
    private ToWsSaleConverter toWsSaleConverter;

    public WsSalePriceDetails convert(SalePriceDetails priceDetails, Sale sale) {
        WsSaleRef saleRef = toWsSaleConverter.reference(sale);
        BigDecimal totalPriceVatExclusivePriorSaleDiscount = priceDetails.getTotalPriceVatExclusivePriorSaleDiscount();
        BigDecimal saleDiscountRatio = priceDetails.getSaleDiscountRatio();
        BigDecimal saleDiscountAmount = priceDetails.getSaleDiscountAmount();
        BigDecimal totalPriceVatExclusive = priceDetails.getTotalPriceVatExclusive();
        BigDecimal vatAmount = priceDetails.getVatAmount();
        BigDecimal totalPriceVatInclusive = priceDetails.getTotalPriceVatInclusive();

        WsSalePriceDetails wsSalePriceDetails = new WsSalePriceDetails();
        wsSalePriceDetails.setSaleRef(saleRef);
        wsSalePriceDetails.setTotalPriceVatExclusivePriorSaleDiscount(totalPriceVatExclusivePriorSaleDiscount);
        wsSalePriceDetails.setSaleDiscountRatio(saleDiscountRatio);
        wsSalePriceDetails.setSaleDiscountAmount(saleDiscountAmount);
        wsSalePriceDetails.setTotalPriceVatExclusive(totalPriceVatExclusive);
        wsSalePriceDetails.setVatAmount(vatAmount);
        wsSalePriceDetails.setTotalPriceVatInclusive(totalPriceVatInclusive);
        return wsSalePriceDetails;
    }
}
