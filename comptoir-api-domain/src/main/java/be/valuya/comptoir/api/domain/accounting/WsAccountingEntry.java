package be.valuya.comptoir.api.domain.accounting;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.model.common.WithId;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AccountingEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountingEntry implements Serializable, WithId {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    @NotNull
    @Nonnull
    private WsAccountRef accountRef;
    @NotNull
    @Nonnull
    private BigDecimal amount;
    private BigDecimal vatRate;
    @NotNull
    @Nonnull
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime dateTime;
    private List<WsLocaleText> description;
    @NotNull
    @Nonnull
    private WsAccountingTransactionRef accountingTransactionRef;
    private WsAccountingEntryRef vatAccountingEntryRef;
    private WsCustomerRef customerRef;

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
    public WsAccountRef getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(@NotNull @Nonnull WsAccountRef accountRef) {
        this.accountRef = accountRef;
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

    public List<WsLocaleText> getDescription() {
        return description;
    }

    public void setDescription(List<WsLocaleText> description) {
        this.description = description;
    }

    @NotNull
    @Nonnull
    public WsAccountingTransactionRef getAccountingTransactionRef() {
        return accountingTransactionRef;
    }

    public void setAccountingTransactionRef(@NotNull @Nonnull WsAccountingTransactionRef accountingTransactionRef) {
        this.accountingTransactionRef = accountingTransactionRef;
    }

    public WsAccountingEntryRef getVatAccountingEntryRef() {
        return vatAccountingEntryRef;
    }

    public void setVatAccountingEntryRef(WsAccountingEntryRef vatAccountingEntryRef) {
        this.vatAccountingEntryRef = vatAccountingEntryRef;
    }

    public WsCustomerRef getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(WsCustomerRef customerRef) {
        this.customerRef = customerRef;
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
        final WsAccountingEntry other = (WsAccountingEntry) obj;
        return Objects.equals(this.id, other.id);
    }

}
