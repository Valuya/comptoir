package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.stock.WsStockRef;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemSaleSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantStockSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;
    private WsItemVariantRef itemVariantRef;
    private WsStockRef stockRef;
    private ZonedDateTime atDateTime;

    @Nonnull
    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(@Nonnull WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsItemVariantRef getItemVariantRef() {
        return itemVariantRef;
    }

    public void setItemVariantRef(WsItemVariantRef itemVariantRef) {
        this.itemVariantRef = itemVariantRef;
    }

    public WsStockRef getStockRef() {
        return stockRef;
    }

    public void setStockRef(WsStockRef stockRef) {
        this.stockRef = stockRef;
    }

    public ZonedDateTime getAtDateTime() {
        return atDateTime;
    }

    public void setAtDateTime(ZonedDateTime atDateTime) {
        this.atDateTime = atDateTime;
    }
}
