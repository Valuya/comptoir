package be.valuya.comptoir.ws.rest.api.domain.accounting;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.util.WithId;
import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
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
    private WsAccountingTransactionType wsAccountingTransactionType;

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
    public WsAccountingTransactionType getWsAccountingTransactionType() {
        return wsAccountingTransactionType;
    }

    public void setWsAccountingTransactionType(@NotNull
                                               @Nonnull WsAccountingTransactionType wsAccountingTransactionType) {
        this.wsAccountingTransactionType = wsAccountingTransactionType;
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
