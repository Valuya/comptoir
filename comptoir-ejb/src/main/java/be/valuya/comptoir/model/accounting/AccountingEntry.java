package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.misc.LocaleText;
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
@Table(name = "accounting_entry")
public class AccountingEntry implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    private BigDecimal vatExclusiveAmount;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @ManyToOne
    private LocaleText description;
    @OneToOne
    private Sale sale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getVatExclusiveAmount() {
        return vatExclusiveAmount;
    }

    public void setVatExclusiveAmount(BigDecimal vatExclusiveAmount) {
        this.vatExclusiveAmount = vatExclusiveAmount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocaleText getDescription() {
        return description;
    }

    public void setDescription(LocaleText description) {
        this.description = description;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
