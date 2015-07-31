package be.valuya.comptoir.model.stock;

import be.valuya.comptoir.model.commercial.Item;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_stock")
public class ItemStock implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime startDateTime;
    @Column(name = "end_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime endDateTime;
    @ManyToOne(optional = false)
    @NotNull
    @Nonnull
    private Stock stock;
    @ManyToOne
    private Item item;
    private BigDecimal quantity;
    @Column(columnDefinition = "TEXT")
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
    public Stock getStock() {
        return stock;
    }

    public void setStock(@Nonnull Stock stock) {
        this.stock = stock;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
        final ItemStock other = (ItemStock) obj;
        return Objects.equals(this.id, other.id);
    }

}
