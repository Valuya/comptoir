package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.thirdparty.Customer;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by cghislai on 24/03/16.
 */
@Entity
@Table(name = "customer_loyalty_accounting_entry")
public class CustomerLoyaltyAccountingEntry implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne
    private Customer customer;
    @NotNull
    @Nonnull
    @OneToOne
    private Sale sale;
    @NotNull
    @Nonnull
    private BigDecimal amount;
    @NotNull
    @Nonnull
    private ZonedDateTime dateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Nonnull
    public Sale getSale() {
        return sale;
    }

    public void setSale(@Nonnull Sale sale) {
        this.sale = sale;
    }

    @Nonnull
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@Nonnull BigDecimal amount) {
        this.amount = amount;
    }

    @Nonnull
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@Nonnull ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerLoyaltyAccountingEntry that = (CustomerLoyaltyAccountingEntry) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
