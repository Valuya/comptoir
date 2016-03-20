package be.valuya.comptoir.model.stock;

import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "itemvariant_stock")
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
    @JoinColumn(name="ITEM_ID")
    @ManyToOne
    private ItemVariant itemVariant;
    private BigDecimal quantity;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @JoinColumn(name = "previous_item_stock_id")
    @OneToOne
    private ItemStock previousItemStock;
    @Column
    @Enumerated(EnumType.STRING)
    private StockChangeType stockChangeType;
    @JoinColumn(name="stock_change_sale_id")
    @ManyToOne
    private Sale stockChangeSale;


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

    public ItemVariant getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(ItemVariant itemVariant) {
        this.itemVariant = itemVariant;
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

    public ItemStock getPreviousItemStock() {
        return previousItemStock;
    }

    public void setPreviousItemStock(ItemStock previousItemStock) {
        this.previousItemStock = previousItemStock;
    }

    public StockChangeType getStockChangeType() {
        return stockChangeType;
    }

    public void setStockChangeType(StockChangeType stockChangeType) {
        this.stockChangeType = stockChangeType;
    }

    public Sale getStockChangeSale() {
        return stockChangeSale;
    }

    public void setStockChangeSale(Sale stockChangeSale) {
        this.stockChangeSale = stockChangeSale;
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
