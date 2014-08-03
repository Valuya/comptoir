package be.valuya.comptoir.model.stock;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_stock")
public class ItemStock implements Serializable {

    @Id
    private Long id;
    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;
    @Column(name = "end_date_time")
    private ZonedDateTime endDateTime;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    private Item item;
    @ManyToOne
    @JoinColumn(name = "previous_item_stock_id")
    private ItemStock previousItemStock;
    private BigDecimal quantity;
    @OneToOne
    @JoinColumn(name = "stock_change_sale_id")
    private Sale stockChangeSale;
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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemStock getPreviousItemStock() {
        return previousItemStock;
    }

    public void setPreviousItemStock(ItemStock previousItemStock) {
        this.previousItemStock = previousItemStock;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Sale getStockChangeSale() {
        return stockChangeSale;
    }

    public void setStockChangeSale(Sale stockChangeSale) {
        this.stockChangeSale = stockChangeSale;
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
