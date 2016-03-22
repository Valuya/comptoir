package be.valuya.comptoir.model.company;

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
@Table(name = "company")
public class Company implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private LocaleText name;
    @ManyToOne(cascade = CascadeType.ALL)
    private LocaleText description;
    @ManyToOne
    @NotNull
    @Nonnull
    private Country country;

    public Company() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocaleText getName() {
        return name;
    }

    public void setName(LocaleText name) {
        this.name = name;
    }

    public LocaleText getDescription() {
        return description;
    }

    public void setDescription(LocaleText description) {
        this.description = description;
    }

    @Nonnull
    public Country getCountry() {
        return country;
    }

    public void setCountry(@Nonnull Country country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final Company other = (Company) obj;
        return Objects.equals(this.id, other.id);
    }

}
