package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "PictureSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsPictureSearch {

    @Nonnull
    private WsCompanyRef companyRef;
    private WsItemRef itemRef;
    private WsItemVariantRef itemVariantRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
    }

    public WsItemVariantRef getItemVariantRef() {
        return itemVariantRef;
    }

    public void setItemVariantRef(WsItemVariantRef itemVariantRef) {
        this.itemVariantRef = itemVariantRef;
    }
}
