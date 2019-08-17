package be.valuya.comptoir.ws.rest.api.domain.commercial;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NotNull
@Schema(name = "WsItemVariantSalePriceDetails")
public class WsItemVariantSalePriceDetails {

    private WsItemVariantSaleRef variantSaleRef;
    private Integer quantity;
    private BigDecimal unitPriceVatExclusive;
    private BigDecimal totalVatExclusivePriorDiscount;

    private BigDecimal discountRatio;
    private BigDecimal effectiveDiscountRatio;
    private BigDecimal discountAmount;
    private BigDecimal totalVatExclusive;

    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalVatInclusive;

    public WsItemVariantSaleRef getVariantSaleRef() {
        return variantSaleRef;
    }

    public void setVariantSaleRef(WsItemVariantSaleRef variantSaleRef) {
        this.variantSaleRef = variantSaleRef;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPriceVatExclusive() {
        return unitPriceVatExclusive;
    }

    public void setUnitPriceVatExclusive(BigDecimal unitPriceVatExclusive) {
        this.unitPriceVatExclusive = unitPriceVatExclusive;
    }

    public BigDecimal getTotalVatExclusivePriorDiscount() {
        return totalVatExclusivePriorDiscount;
    }

    public void setTotalVatExclusivePriorDiscount(BigDecimal totalVatExclusivePriorDiscount) {
        this.totalVatExclusivePriorDiscount = totalVatExclusivePriorDiscount;
    }

    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    public BigDecimal getEffectiveDiscountRatio() {
        return effectiveDiscountRatio;
    }

    public void setEffectiveDiscountRatio(BigDecimal effectiveDiscountRatio) {
        this.effectiveDiscountRatio = effectiveDiscountRatio;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalVatExclusive() {
        return totalVatExclusive;
    }

    public void setTotalVatExclusive(BigDecimal totalVatExclusive) {
        this.totalVatExclusive = totalVatExclusive;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalVatInclusive() {
        return totalVatInclusive;
    }

    public void setTotalVatInclusive(BigDecimal totalVatInclusive) {
        this.totalVatInclusive = totalVatInclusive;
    }
}
