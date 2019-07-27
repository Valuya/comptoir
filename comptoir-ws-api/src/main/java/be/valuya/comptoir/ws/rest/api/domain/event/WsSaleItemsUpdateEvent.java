package be.valuya.comptoir.ws.rest.api.domain.event;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "saleUpdate")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A sale update event", name = "WsSaleItemsUpdateEvent")
public class WsSaleItemsUpdateEvent extends WsComptoirServerEvent {

    private WsSaleRef saleRef;
    private List<WsItemVariantSale> firstPage;
    private Long pageSize;
    private Long totalCount;

    public WsSaleItemsUpdateEvent() {
        super(WsComptoirEvent.SALE_ITEMS);
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public List<WsItemVariantSale> getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(List<WsItemVariantSale> firstPage) {
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
