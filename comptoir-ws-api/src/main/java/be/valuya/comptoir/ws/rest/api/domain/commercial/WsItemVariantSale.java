package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.ws.rest.api.util.WithId;
import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemVariantSale")
@XmlAccessorType(XmlAccessType.FIELD)
@Nullable
public class WsItemVariantSale implements WithId {

    private Long id;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime dateTime;
    private WsItemVariantRef itemVariantRef;
    private WsSaleRef saleRef;
    private List<WsLocaleText> comment;
    private Boolean includeCustomerLoyalty;
    private Boolean includeCustomerDiscount;
    private WsStockRef stockRef;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public WsItemVariantRef getItemVariantRef() {
        return itemVariantRef;
    }

    public void setItemVariantRef(WsItemVariantRef itemVariantRef) {
        this.itemVariantRef = itemVariantRef;
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public List<WsLocaleText> getComment() {
        return comment;
    }

    public void setComment(List<WsLocaleText> comment) {
        this.comment = comment;
    }

    public Boolean getIncludeCustomerLoyalty() {
        return includeCustomerLoyalty;
    }

    public void setIncludeCustomerLoyalty(Boolean includeCustomerLoyalty) {
        this.includeCustomerLoyalty = includeCustomerLoyalty;
    }


    public Boolean getIncludeCustomerDiscount() {
        return includeCustomerDiscount;
    }

    public void setIncludeCustomerDiscount(Boolean includeCustomerDiscount) {
        this.includeCustomerDiscount = includeCustomerDiscount;
    }

    public WsStockRef getStockRef() {
        return stockRef;
    }

    public void setStockRef(WsStockRef stockRef) {
        this.stockRef = stockRef;
    }

}
