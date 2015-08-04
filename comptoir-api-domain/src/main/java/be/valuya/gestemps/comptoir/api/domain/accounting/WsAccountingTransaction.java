package be.valuya.gestemps.comptoir.api.domain.accounting;

import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.gestemps.comptoir.api.domain.company.WsCompanyRef;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "accounting_transaction")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountingTransaction implements Serializable {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    private ZonedDateTime dateTime;
    @NotNull
    @Nonnull
    private AccountingTransactionType accountingTransactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Nonnull
    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(@NotNull @Nonnull WsCompanyRef companyRef) {
        this.companyRef = companyRef;
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
        final WsAccountingTransaction other = (WsAccountingTransaction) obj;
        return Objects.equals(this.id, other.id);
    }

}
