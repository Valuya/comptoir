package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.model.lang.LocaleText;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "attribute_value")
public class AttributeValue implements Serializable, WithId {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private LocaleText value;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attribute_definition_id")
    private AttributeDefinition attributeDefinition;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocaleText getValue() {
        return value;
    }

    public void setValue(LocaleText value) {
        this.value = value;
    }

    public AttributeDefinition getAttributeDefinition() {
        return attributeDefinition;
    }

    public void setAttributeDefinition(AttributeDefinition attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
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
        final AttributeValue other = (AttributeValue) obj;
        return Objects.equals(this.id, other.id);
    }

}
