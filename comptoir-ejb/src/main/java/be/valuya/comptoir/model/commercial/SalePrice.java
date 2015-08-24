package be.valuya.comptoir.model.commercial;

import java.math.BigDecimal;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class SalePrice {

    private BigDecimal base;
    private BigDecimal taxes;

    public SalePrice() {
        this(BigDecimal.ZERO.setScale(2), BigDecimal.ZERO.setScale(2));
    }

    public SalePrice(BigDecimal base, BigDecimal taxes) {
        this.base = base;
        this.taxes = taxes;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

}
