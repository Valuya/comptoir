package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WithId;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "Sale")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsSale implements WithId {

    private Long id;
    private WsCompanyRef companyRef;
    private WsCustomerRef customerRef;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime dateTime;
    private WsInvoiceRef invoiceRef;
    private BigDecimal vatExclusiveAmount;
    private BigDecimal vatAmount;
    private boolean closed;
    @Size(max = 128)
    private String reference;
    private WsAccountingTransactionRef accountingTransactionRef;
    private BigDecimal discountRatio;
    private BigDecimal discountAmount;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsCustomerRef getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(WsCustomerRef customerRef) {
        this.customerRef = customerRef;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public WsInvoiceRef getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(WsInvoiceRef invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public BigDecimal getVatExclusiveAmount() {
        return vatExclusiveAmount;
    }

    public void setVatExclusiveAmount(BigDecimal vatExclusiveAmount) {
        this.vatExclusiveAmount = vatExclusiveAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
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

    public WsAccountingTransactionRef getAccountingTransactionRef() {
        return accountingTransactionRef;
    }

    public void setAccountingTransactionRef(WsAccountingTransactionRef accountingTransactionRef) {
        this.accountingTransactionRef = accountingTransactionRef;
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
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final WsSale other = (WsSale) obj;
        return Objects.equals(this.id, other.id);
    }

}
