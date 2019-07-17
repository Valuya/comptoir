package be.valuya.comptoir.ws.rest.api.domain.commercial;

import java.math.BigDecimal;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "SalePrice")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsSalePrice {

    @Nonnull
    @NotNull
    private BigDecimal base;
    @Nonnull
    @NotNull
    private BigDecimal taxes;

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
