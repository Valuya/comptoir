package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.lang.LocaleText;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_sale")
public class ItemSale implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "date_time", columnDefinition = "DATETIME")
    private ZonedDateTime dateTime;
    @ManyToOne
    private Item item;
    @ManyToOne(cascade = CascadeType.ALL)
    private Price price;
    private BigDecimal quantity;
    @ManyToOne
    private Sale sale;
    @Column(name = "discount_ratio")
    private BigDecimal discountRatio;
    @ManyToOne
    private LocaleText comment;
    @OneToOne
    private AccountingEntry accountingEntry;

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public LocaleText getComment() {
        return comment;
    }

    public void setComment(LocaleText comment) {
        this.comment = comment;
    }

    public AccountingEntry getAccountingEntry() {
        return accountingEntry;
    }

    public void setAccountingEntry(AccountingEntry accountingEntry) {
        this.accountingEntry = accountingEntry;
    }

    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final ItemSale other = (ItemSale) obj;
        return Objects.equals(this.id, other.id);
    }
}
