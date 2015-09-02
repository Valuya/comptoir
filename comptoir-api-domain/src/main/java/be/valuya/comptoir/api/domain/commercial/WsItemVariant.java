package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.model.common.WithId;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemVariant")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariant implements WithId {

    private Long id;
    private WsItemPictureRef mainPictureRef;
    @NotNull
    @Size(max = 128)
    private String variantReference;
    @NotNull
    private Pricing pricing;
    private BigDecimal pricingAmount;
    private List<WsAttributeValue> attributeValues;
    private WsItemRef itemRef;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public WsItemPictureRef getMainPictureRef() {
        return mainPictureRef;
    }

    public void setMainPictureRef(WsItemPictureRef mainPictureRef) {
        this.mainPictureRef = mainPictureRef;
    }

    public String getVariantReference() {
        return variantReference;
    }

    public void setVariantReference(String variantReference) {
        this.variantReference = variantReference;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public BigDecimal getPricingAmount() {
        return pricingAmount;
    }

    public void setPricingAmount(BigDecimal pricingAmount) {
        this.pricingAmount = pricingAmount;
    }

    public List<WsAttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<WsAttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsItemVariant other = (WsItemVariant) obj;
        return Objects.equals(this.id, other.id);
    }

}
