package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.thirdparty.Customer;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Sale implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    private Customer customer;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @OneToOne(mappedBy = "sale", fetch = FetchType.LAZY)
    private Invoice invoice;
    @OneToOne
    private AccountingEntry accountingEntry;

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

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public AccountingEntry getAccountingEntry() {
        return accountingEntry;
    }

    public void setAccountingEntry(AccountingEntry accountingEntry) {
        this.accountingEntry = accountingEntry;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Sale other = (Sale) obj;
        return Objects.equals(this.id, other.id);
    }

}
