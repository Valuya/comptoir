package be.valuya.comptoir.model.commercial;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NotNull
public class ItemVariantSalePriceDetails {

    private int quantity;
    private BigDecimal unitPriceVatExclusive;
    private BigDecimal totalVatExclusivePriorDiscount;

    private BigDecimal discountRatio;
    private BigDecimal effectiveDiscountRatio;
    private BigDecimal discountAmount;
    private BigDecimal totalVatExclusive;

    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalVatInclusive;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
