package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Sale implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    private Company company;
    @ManyToOne
    private Customer customer;
    @Column(name = "date_time", columnDefinition = "DATETIME")
    private ZonedDateTime dateTime;
    @OneToOne(mappedBy = "sale", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Invoice invoice;
    @NotNull
    @Nonnull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accounting_transaction_id")
    private AccountingTransaction accountingTransaction;
    @NotNull
    @Nonnull
    @Column(name = "vat_exclusive_amount")
    private BigDecimal vatExclusiveAmout;
    @NotNull
    @Nonnull
    @Column(name = "vat_amount")
    private BigDecimal vatAmount;
    private boolean closed;
    @OneToOne
    @JoinColumn(name = "vat_accounting_entry_id")
    private AccountingEntry vatAccountingEntry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    @NotNull
    @Nonnull
    public AccountingTransaction getAccountingTransaction() {
        return accountingTransaction;
    }

    public void setAccountingTransaction(@NotNull @Nonnull AccountingTransaction accountingTransaction) {
        this.accountingTransaction = accountingTransaction;
    }

    @NotNull
    @Nonnull
    public BigDecimal getVatExclusiveAmout() {
        return vatExclusiveAmout;
    }

    public void setVatExclusiveAmout(@NotNull @Nonnull BigDecimal vatExclusiveAmout) {
        this.vatExclusiveAmout = vatExclusiveAmout;
    }

    @NotNull
    @Nonnull
    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(@NotNull @Nonnull BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public AccountingEntry getVatAccountingEntry() {
        return vatAccountingEntry;
    }

    public void setVatAccountingEntry(AccountingEntry vatAccountingEntry) {
        this.vatAccountingEntry = vatAccountingEntry;
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
