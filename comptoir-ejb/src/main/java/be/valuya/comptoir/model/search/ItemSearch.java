package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;
import java.util.Locale;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemSearch {

    @NotNull
    @Nonnull
    private Company company;
    private String nameContains;
    private String descriptionContains;
    private String reference;
    private String referenceContains;
    private String multiSearch;
    @CheckForNull
    private Locale locale;
    private Boolean multipleSale;

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceContains() {
        return referenceContains;
    }

    public void setReferenceContains(String referenceContains) {
        this.referenceContains = referenceContains;
    }

    public String getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(String multiSearch) {
        this.multiSearch = multiSearch;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Boolean getMultipleSale() {
        return multipleSale;
    }

    public void setMultipleSale(Boolean multipleSale) {
        this.multipleSale = multipleSale;
    }

}
