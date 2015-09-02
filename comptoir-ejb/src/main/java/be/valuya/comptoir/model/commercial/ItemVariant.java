package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.common.WithId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_variant")
public class ItemVariant implements Serializable, WithId {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Item item;
    @NotNull
    @Column(length = 128)
    @Size(max = 128)
    private String variantReference;
    @Enumerated(EnumType.STRING)
    private Pricing pricing;
    private BigDecimal pricingAmount;
    @ManyToOne
    @JoinColumn(name = "main_picture_id")
    private ItemPicture mainPicture;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "item_attribute_value")
    private List<AttributeValue> attributeValues;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getVariantReference() {
        return variantReference;
    }

    public void setVariantReference(String variantReference) {
        this.variantReference = variantReference;
    }

    public ItemPicture getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(ItemPicture mainPicture) {
        this.mainPicture = mainPicture;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getPricingAmount() {
        return pricingAmount;
    }

    public void setPricingAmount(BigDecimal pricingAmount) {
        this.pricingAmount = pricingAmount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final ItemVariant other = (ItemVariant) obj;
        return Objects.equals(this.id, other.id);
    }

}
