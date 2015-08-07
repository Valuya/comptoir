package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WithId;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "sale")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsSale implements WithId {

    private Long id;
    private WsCustomerRef customerRef;
    private ZonedDateTime dateTime;
    private WsInvoiceRef invoiceRef;
    private BigDecimal vatExclusiveAmout;
    private BigDecimal vatAmount;
    private boolean closed;
    private String reference;
    private WsAccountingTransactionRef accountingTransactionRef;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getVatExclusiveAmout() {
        return vatExclusiveAmout;
    }

    public void setVatExclusiveAmout(BigDecimal vatExclusiveAmout) {
        this.vatExclusiveAmout = vatExclusiveAmout;
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

}
