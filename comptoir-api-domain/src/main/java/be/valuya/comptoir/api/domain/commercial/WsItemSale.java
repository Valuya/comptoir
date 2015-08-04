package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "itemSale")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemSale {

    private Long id;
    private ZonedDateTime dateTime;
    private WsItemRef itemRef;
    private WsPrice price;
    private BigDecimal quantity;
    private WsSaleRef saleRef;
    private WsLocaleText comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
    }

    public WsPrice getPrice() {
        return price;
    }

    public void setPrice(WsPrice price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public WsLocaleText getComment() {
        return comment;
    }

    public void setComment(WsLocaleText comment) {
        this.comment = comment;
    }

}
