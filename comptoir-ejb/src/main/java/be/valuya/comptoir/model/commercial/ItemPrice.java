package be.valuya.comptoir.model.commercial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_price")
public class ItemPrice implements Serializable {

    @Id
    private Long id;
    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;
    @Column(name = "end_date_time")
    private ZonedDateTime endDateTime;
    @ManyToOne
    private Item item;
    @NotNull
    @Nonnull
    private BigDecimal vatExclusive;
    @NotNull
    @Nonnull
    private BigDecimal vatRate = BigDecimal.valueOf(21L, 2);

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @NotNull
    @Nonnull
    public BigDecimal getVatExclusive() {
        return vatExclusive;
    }

    public void setVatExclusive(@NotNull @Nonnull BigDecimal vatExclusive) {
        this.vatExclusive = vatExclusive;
    }

    @NotNull
    @Nonnull
    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(@NotNull @Nonnull BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final ItemPrice other = (ItemPrice) obj;
        return Objects.equals(this.id, other.id);
    }

}
