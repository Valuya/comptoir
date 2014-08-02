package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemSearch {

    @NotNull
    @Nonnull
    private Company company;
    private String nameContains;
    private String descriptionContains;
    private String barCode;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getNameContains() {
        return nameContains;
    }

    public void setNameContains(String nameContains) {
        this.nameContains = nameContains;
    }

    public String getDescriptionContains() {
        return descriptionContains;
    }

    public void setDescriptionContains(String descriptionContains) {
        this.descriptionContains = descriptionContains;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

}
