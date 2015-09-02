package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Item implements Serializable, WithId {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(optional = false)
    private Company company;
    @NotNull
    @Column(length = 128)
    @Size(max = 128)
    private String reference;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private LocaleText name;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private LocaleText description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_price_id")
    private Price currentPrice;
    @ManyToOne
    @JoinColumn(name = "main_picture_id")
    private ItemPicture mainPicture;

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public Price getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
    }

    public ItemPicture getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(ItemPicture mainPicture) {
        this.mainPicture = mainPicture;
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
        final Item other = (Item) obj;
        return Objects.equals(this.id, other.id);
    }

}
