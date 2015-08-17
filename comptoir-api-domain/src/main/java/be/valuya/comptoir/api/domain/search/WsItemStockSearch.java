package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.stock.Stock;
import java.time.ZonedDateTime;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemStockSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemStockSearch {

    @CheckForNull
    private Item item;
    @CheckForNull
    private Stock stock;
    @CheckForNull
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime atDateTime;
    @Nonnull
    private WsCompanyRef companyRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    @CheckForNull
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @CheckForNull
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @CheckForNull
    public ZonedDateTime getAtDateTime() {
        return atDateTime;
    }

    public void setAtDateTime(ZonedDateTime atDateTime) {
        this.atDateTime = atDateTime;
    }

}
