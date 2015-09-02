package be.valuya.comptoir.api.domain.accounting;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AccountingTransaction")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountingTransaction implements Serializable, WithId {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime dateTime;
    @NotNull
    @Nonnull
    private AccountingTransactionType accountingTransactionType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
