package be.valuya.gestemps.comptoir.api.domain.stock;

import be.valuya.gestemps.comptoir.api.domain.commercial.WsItemRef;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemStock")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemStock implements Serializable {

    private Long id;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private WsStockRef stockRef;
    private WsItemRef itemRef;
    private BigDecimal quantity;
    private String comment;

    public Long getId() {
        return id;
    }

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

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
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
