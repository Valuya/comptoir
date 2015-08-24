package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.domain.company.WithId;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
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
@XmlRootElement(name = "ItemSale")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemSale implements WithId {

    private Long id;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime dateTime;
    private WsItemRef itemRef;
    private BigDecimal quantity;
    private BigDecimal total;
    private WsSaleRef saleRef;
    private List<WsLocaleText> comment;
    @NotNull
    @Nonnull
    private BigDecimal vatExclusive;
    @NotNull
    @Nonnull
    private BigDecimal vatRate;
    private BigDecimal discountRatio;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
    }

    public BigDecimal getVatExclusive() {
        return vatExclusive;
    }

    public void setVatExclusive(BigDecimal vatExclusive) {
        this.vatExclusive = vatExclusive;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public List<WsLocaleText> getComment() {
        return comment;
    }

    public void setComment(List<WsLocaleText> comment) {
        this.comment = comment;
    }

    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

}
