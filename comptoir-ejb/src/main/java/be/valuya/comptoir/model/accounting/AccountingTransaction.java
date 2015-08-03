package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.company.Company;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "accounting_transaction")
public class AccountingTransaction implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    private Company company;
    @Column(name = "date_time", columnDefinition = "DATETIME")
    private ZonedDateTime dateTime;
    @NotNull
    @Nonnull
    @Enumerated(EnumType.STRING)
    private AccountingTransactionType accountingTransactionType;

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
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NotNull
            @Nonnull ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @NotNull
    @Nonnull
    public AccountingTransactionType getAccountingTransactionType() {
        return accountingTransactionType;
    }

    public void setAccountingTransactionType(@NotNull
            @Nonnull AccountingTransactionType accountingTransactionType) {
        this.accountingTransactionType = accountingTransactionType;
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
        final AccountingTransaction other = (AccountingTransaction) obj;
        return Objects.equals(this.id, other.id);
    }

}
