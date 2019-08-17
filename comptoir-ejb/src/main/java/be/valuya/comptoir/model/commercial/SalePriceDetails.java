package be.valuya.comptoir.model.commercial;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NotNull
public class SalePriceDetails {

    private BigDecimal totalPriceVatExclusivePriorSaleDiscount;
    private BigDecimal saleDiscountRatio;
    private BigDecimal saleDiscountAmount;
    private BigDecimal totalPriceVatExclusive;
    private BigDecimal vatAmount;
    private BigDecimal totalPriceVatInclusive;

    public BigDecimal getTotalPriceVatExclusivePriorSaleDiscount() {
        return totalPriceVatExclusivePriorSaleDiscount;
    }

    public void setTotalPriceVatExclusivePriorSaleDiscount(BigDecimal totalPriceVatExclusivePriorSaleDiscount) {
        this.totalPriceVatExclusivePriorSaleDiscount = totalPriceVatExclusivePriorSaleDiscount;
    }

    public BigDecimal getSaleDiscountRatio() {
        return saleDiscountRatio;
    }

    public void setSaleDiscountRatio(BigDecimal saleDiscountRatio) {
        this.saleDiscountRatio = saleDiscountRatio;
    }

    public BigDecimal getSaleDiscountAmount() {
        return saleDiscountAmount;
    }

    public void setSaleDiscountAmount(BigDecimal saleDiscountAmount) {
        this.saleDiscountAmount = saleDiscountAmount;
    }

    public BigDecimal getTotalPriceVatExclusive() {
        return totalPriceVatExclusive;
    }

    public void setTotalPriceVatExclusive(BigDecimal totalPriceVatExclusive) {
        this.totalPriceVatExclusive = totalPriceVatExclusive;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalPriceVatInclusive() {
        return totalPriceVatInclusive;
    }

    public void setTotalPriceVatInclusive(BigDecimal totalPriceVatInclusive) {
        this.totalPriceVatInclusive = totalPriceVatInclusive;
    }
}
