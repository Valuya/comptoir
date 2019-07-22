package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "attribute_definition")
public class AttributeDefinition implements Serializable, WithId, WithCompany {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Company company;
    @NotNull
    @Nonnull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private LocaleText name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocaleText getName() {
        return name;
    }

    public void setName(LocaleText name) {
        this.name = name;
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
        final AttributeDefinition other = (AttributeDefinition) obj;
        return Objects.equals(this.id, other.id);
    }

}
