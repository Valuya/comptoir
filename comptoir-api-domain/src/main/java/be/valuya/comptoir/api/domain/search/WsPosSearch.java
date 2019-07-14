package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "PosSearch")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "Point of sale filter")
public class WsPosSearch {

    @Nonnull
    @Schema(required = true, implementation = WsCompanyRef.class)
    private WsCompanyRef companyRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

}
