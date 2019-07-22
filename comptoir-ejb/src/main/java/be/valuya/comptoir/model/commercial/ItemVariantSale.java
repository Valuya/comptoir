package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "itemvariant_sale")
public class ItemVariantSale implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "date_time", columnDefinition = "DATETIME")
    private ZonedDateTime dateTime;
    @ManyToOne
    private ItemVariant itemVariant;
    @ManyToOne(cascade = CascadeType.ALL)
    private Price price;
    private BigDecimal quantity;
    private BigDecimal total;
    @ManyToOne
    private Sale sale;
    @ManyToOne(cascade = CascadeType.ALL)
    private LocaleText comment;
    @OneToOne
    private AccountingEntry accountingEntry;
    @ManyToOne
    @NotNull
    private Stock stock;
    @Column(name = "CUSTOMER_LOYALTY")
    private Boolean includeCustomerLoyalty;

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

    public ItemVariant getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(ItemVariant itemVariant) {
        this.itemVariant = itemVariant;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Boolean getIncludeCustomerLoyalty() {
        return includeCustomerLoyalty;
    }

    public void setIncludeCustomerLoyalty(Boolean customerLoyalty) {
        this.includeCustomerLoyalty = customerLoyalty;
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
        final ItemVariantSale other = (ItemVariantSale) obj;
        return Objects.equals(this.id, other.id);
    }
}
