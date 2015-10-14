package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;
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
public class AttributeSearch {

    @NotNull
    @Nonnull
    private Company company;
    private String nameContains;
    private String valueContains;
    private String multiSearch;
    @CheckForNull
    private LocaleSearch localeSearch;

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

    public String getValueContains() {
        return valueContains;
    }

    public void setValueContains(String valueContains) {
        this.valueContains = valueContains;
    }

    public String getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(String multiSearch) {
        this.multiSearch = multiSearch;
    }

    @CheckForNull
    public LocaleSearch getLocaleSearch() {
        return localeSearch;
    }

    public void setLocaleSearch(LocaleSearch localeSearch) {
        this.localeSearch = localeSearch;
    }

}
