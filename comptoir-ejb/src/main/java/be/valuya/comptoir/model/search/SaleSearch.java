package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class SaleSearch {

    @NotNull
    @Nonnull
    private Company company;
    private Boolean closed;
    @CheckForNull
    private LocaleSearch localeSearch;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @CheckForNull
    public LocaleSearch getLocaleSearch() {
        return localeSearch;
    }

    public void setLocaleSearch(LocaleSearch localeSearch) {
        this.localeSearch = localeSearch;
    }
}
