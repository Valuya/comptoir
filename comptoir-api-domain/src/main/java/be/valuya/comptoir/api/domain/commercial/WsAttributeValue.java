package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AttributeValue")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAttributeValue implements Serializable, WithId {

    private Long id;
    @NotNull
    @Nonnull
    private WsAttributeDefinitionRef attributeDefinitionRef;
    private WsLocaleText value;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsAttributeDefinitionRef getAttributeDefinitionRef() {
        return attributeDefinitionRef;
    }

    public void setAttributeDefinitionRef(WsAttributeDefinitionRef attributeDefinitionRef) {
        this.attributeDefinitionRef = attributeDefinitionRef;
    }

    public WsLocaleText getValue() {
        return value;
    }

    public void setValue(WsLocaleText value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final WsAttributeValue other = (WsAttributeValue) obj;
        return Objects.equals(this.id, other.id);
    }

}
