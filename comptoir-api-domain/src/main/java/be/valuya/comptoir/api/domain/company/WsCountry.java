package be.valuya.comptoir.api.domain.company;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "company")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A country")
public class WsCountry {

    @Schema(description = "The country 2-letter code")
    private String code;
    private BigDecimal defaultVatRate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDefaultVatRate() {
        return defaultVatRate;
    }

    public void setDefaultVatRate(BigDecimal defaultVatRate) {
        this.defaultVatRate = defaultVatRate;
    }

}
