package be.valuya.comptoir.ws.rest.api.domain.commercial;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NotNull
public class WsSalePriceDetails {

    private WsSaleRef saleRef;
    private BigDecimal totalPriceVatExclusivePriorSaleDiscount;
    private BigDecimal saleDiscountRatio;
    private BigDecimal saleDiscountAmount;
    private BigDecimal totalPriceVatExclusive;
    private BigDecimal vatAmount;
    private BigDecimal totalPriceVatInclusive;

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

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
