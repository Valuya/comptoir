package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemSaleSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemSaleSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;
    private WsItemRef itemRef;
    private WsSaleRef saleRef;

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

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }
}
