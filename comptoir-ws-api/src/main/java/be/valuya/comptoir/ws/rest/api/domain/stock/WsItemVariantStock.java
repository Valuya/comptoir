package be.valuya.comptoir.ws.rest.api.domain.stock;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.ws.rest.api.util.WithId;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemStock")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantStock implements Serializable, WithId {

    private Long id;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime startDateTime;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime endDateTime;
    @CheckForNull
    private Integer orderPosition;
    private WsStockRef stockRef;
    private WsItemVariantRef itemVariantRef;
    private BigDecimal quantity;
    private String comment;
    private WsItemVariantStockRef previousItemStockRef;
    private WsStockChangeType stockChangeType;
    private WsItemVariantSaleRef stockChangeVariantSaleRef;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Nonnull
    public WsStockRef getStockRef() {
        return stockRef;
    }

    public void setStockRef(@Nonnull WsStockRef stockRef) {
        this.stockRef = stockRef;
    }

    public WsItemVariantRef getItemVariantRef() {
        return itemVariantRef;
    }

    public void setItemVariantRef(WsItemVariantRef itemVariantRef) {
        this.itemVariantRef = itemVariantRef;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public WsItemVariantStockRef getPreviousItemStockRef() {
        return previousItemStockRef;
    }

    public void setPreviousItemStockRef(WsItemVariantStockRef previousItemStockRef) {
        this.previousItemStockRef = previousItemStockRef;
    }

    public WsItemVariantSaleRef getStockChangeVariantSaleRef() {
        return stockChangeVariantSaleRef;
    }

    public void setStockChangeVariantSaleRef(WsItemVariantSaleRef stockChangeVariantSaleRef) {
        this.stockChangeVariantSaleRef = stockChangeVariantSaleRef;
    }

    public WsStockChangeType getStockChangeType() {
        return stockChangeType;
    }

    public void setStockChangeType(WsStockChangeType stockChangeType) {
        this.stockChangeType = stockChangeType;
    }

    public Integer getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(Integer orderPosition) {
        this.orderPosition = orderPosition;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsItemVariantStock other = (WsItemVariantStock) obj;
        return Objects.equals(this.id, other.id);
    }

}
