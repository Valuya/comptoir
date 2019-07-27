package be.valuya.comptoir.ws.rest.api.domain.event;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "saleUpdate")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A sale update event", name = "WsSaleUpdateEvent")
public class WsSaleUpdateEvent extends WsComptoirServerEvent {

    private WsSale wsSale;
    private BigDecimal totalVatInclusive;
    private BigDecimal totalPaid;

    public WsSaleUpdateEvent() {
        super(WsComptoirEvent.SALE_UPDATE);
    }

    public WsSale getWsSale() {
        return wsSale;
    }

    public void setWsSale(WsSale wsSale) {
        this.wsSale = wsSale;
    }

    public BigDecimal getTotalVatInclusive() {
        return totalVatInclusive;
    }

    public void setTotalVatInclusive(BigDecimal totalVatInclusive) {
        this.totalVatInclusive = totalVatInclusive;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }
}
