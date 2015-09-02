package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
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
@XmlRootElement(name = "AttributeDefinition")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAttributeDefinition implements Serializable, WithId {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    private WsLocaleText name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsLocaleText getName() {
        return name;
    }

    public void setName(WsLocaleText name) {
        this.name = name;
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
        final WsAttributeDefinition other = (WsAttributeDefinition) obj;
        return Objects.equals(this.id, other.id);
    }

}
