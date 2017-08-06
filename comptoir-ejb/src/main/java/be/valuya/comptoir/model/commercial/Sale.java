package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "sale")
public class Sale implements Serializable, WithCompany {

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accounting_transaction_id")
    private AccountingTransaction accountingTransaction;
    @NotNull
    @Nonnull
    @Column(name = "vat_exclusive_amount")
    private BigDecimal vatExclusiveAmount;
    @NotNull
    @Nonnull
    @Column(name = "vat_amount")
    private BigDecimal vatAmount;
    private boolean closed;
    @Size(max = 128)
    private String reference;
    @Column(name = "discount_ratio")
    private BigDecimal discountRatio;
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

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
    public AccountingTransaction getAccountingTransaction() {
        return accountingTransaction;
    }

    public void setAccountingTransaction(@NotNull AccountingTransaction accountingTransaction) {
        this.accountingTransaction = accountingTransaction;
    }

    @NotNull
    @Nonnull
    public BigDecimal getVatExclusiveAmount() {
        return vatExclusiveAmount;
    }

    public void setVatExclusiveAmount(@NotNull @Nonnull BigDecimal vatExclusiveAmount) {
        this.vatExclusiveAmount = vatExclusiveAmount;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
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
