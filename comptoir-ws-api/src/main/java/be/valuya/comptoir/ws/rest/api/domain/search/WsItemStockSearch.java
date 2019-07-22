package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.ZonedDateTime;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemStockSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemStockSearch {

    @CheckForNull
    private WsItemVariantRef itemVariantRef;
    @CheckForNull
    private WsStockRef stockRef;
    @CheckForNull
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime atDateTime;
    @Nonnull
    private WsCompanyRef companyRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    @CheckForNull
    public WsItemVariantRef getItemVariantRef() {
        return itemVariantRef;
    }

    public void setItemVariantRef(@CheckForNull WsItemVariantRef itemVariantRef) {
        this.itemVariantRef = itemVariantRef;
    }

    @CheckForNull
    public WsStockRef getStockRef() {
        return stockRef;
    }

    public void setStockRef(@CheckForNull WsStockRef stockRef) {
        this.stockRef = stockRef;
    }

    @CheckForNull
    public ZonedDateTime getAtDateTime() {
        return atDateTime;
    }

    public void setAtDateTime(ZonedDateTime atDateTime) {
        this.atDateTime = atDateTime;
    }

}
