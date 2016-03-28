package be.valuya.comptoir.api.domain.stock;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.model.stock.StockChangeType;

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
public class WsItemStock implements Serializable, WithId {

    private Long id;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime startDateTime;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime endDateTime;
    private WsStockRef stockRef;
    private WsItemVariantRef itemVariantRef;
    private BigDecimal quantity;
    private String comment;
    private WsItemStockRef previousItemStockRef;
    private StockChangeType stockChangeType;
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

    public WsItemStockRef getPreviousItemStockRef() {
        return previousItemStockRef;
    }

    public void setPreviousItemStockRef(WsItemStockRef previousItemStockRef) {
        this.previousItemStockRef = previousItemStockRef;
    }

    public WsItemVariantSaleRef getStockChangeVariantSaleRef() {
        return stockChangeVariantSaleRef;
    }

    public void setStockChangeVariantSaleRef(WsItemVariantSaleRef stockChangeVariantSaleRef) {
        this.stockChangeVariantSaleRef = stockChangeVariantSaleRef;
    }

    public StockChangeType getStockChangeType() {
        return stockChangeType;
    }

    public void setStockChangeType(StockChangeType stockChangeType) {
        this.stockChangeType = stockChangeType;
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
        final WsItemStock other = (WsItemStock) obj;
        return Objects.equals(this.id, other.id);
    }

}
