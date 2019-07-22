package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.annotation.Nonnull;
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
    @Schema(required = true)
    private WsCompanyRef companyRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

}
