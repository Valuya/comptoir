package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PictureSearch implements WithCompany {

    @NotNull
    @Nonnull
    private Company company;
    private Item item;
    private ItemVariant itemVariant;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemVariant getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(ItemVariant itemVariant) {
        this.itemVariant = itemVariant;
    }
}
