package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.misc.LocaleText;
import be.valuya.comptoir.model.thirdparty.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "accounting_entry")
public class AccountingEntry implements Serializable {

    @Id
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne
    private Company company;
    @NotNull
    @Nonnull
    @ManyToOne
    private Account account;
    @NotNull
    @Nonnull
    private BigDecimal amount;
    private BigDecimal vatRate;
    @NotNull
    @Nonnull
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @ManyToOne(cascade = CascadeType.ALL)
    private LocaleText description;
    @NotNull
    @Nonnull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accounting_transaction_id")
    private AccountingTransaction accountingTransaction;
    @OneToOne(cascade = CascadeType.ALL)
    private AccountingEntry vatAccountingEntry;
    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Nonnull
    public Company getCompany() {
        return company;
    }

    public void setCompany(@NotNull
            @Nonnull Company company) {
        this.company = company;
    }

    @NotNull
    @Nonnull
    public Account getAccount() {
        return account;
    }

    public void setAccount(@NotNull
            @Nonnull Account account) {
        this.account = account;
    }

    @NotNull
    @Nonnull
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotNull
            @Nonnull BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    @NotNull
    @Nonnull
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NotNull
            @Nonnull ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocaleText getDescription() {
        return description;
    }

    public void setDescription(LocaleText description) {
        this.description = description;
    }

    @NotNull
    @Nonnull
    public AccountingTransaction getAccountingTransaction() {
        return accountingTransaction;
    }

    public void setAccountingTransaction(@NotNull @Nonnull AccountingTransaction accountingTransaction) {
        this.accountingTransaction = accountingTransaction;
    }

    public AccountingEntry getVatAccountingEntry() {
        return vatAccountingEntry;
    }

    public void setVatAccountingEntry(AccountingEntry vatAccountingEntry) {
        this.vatAccountingEntry = vatAccountingEntry;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final AccountingEntry other = (AccountingEntry) obj;
        return Objects.equals(this.id, other.id);
    }

}
