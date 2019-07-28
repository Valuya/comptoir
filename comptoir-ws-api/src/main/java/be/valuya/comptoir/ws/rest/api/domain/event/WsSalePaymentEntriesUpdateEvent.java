package be.valuya.comptoir.ws.rest.api.domain.event;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransaction;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "WsSalePaymentEntriesUpdateEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A sale update event", name = "WsSalePaymentEntriesUpdateEvent")
public class WsSalePaymentEntriesUpdateEvent extends WsComptoirServerEvent {

    private WsSaleRef saleRef;
    private WsAccountingTransactionRef accountingTransactionRef;
    private List<WsAccountingEntry> firstPage;
    private Long pageSize;
    private Long totalCount;

    public WsSalePaymentEntriesUpdateEvent() {
        super(WsComptoirEvent.SALE_PAYMENT_ENTRIES);
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public WsAccountingTransactionRef getAccountingTransactionRef() {
        return accountingTransactionRef;
    }

    public void setAccountingTransactionRef(WsAccountingTransactionRef accountingTransactionRef) {
        this.accountingTransactionRef = accountingTransactionRef;
    }

    public List<WsAccountingEntry> getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(List<WsAccountingEntry> firstPage) {
        this.firstPage = firstPage;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
