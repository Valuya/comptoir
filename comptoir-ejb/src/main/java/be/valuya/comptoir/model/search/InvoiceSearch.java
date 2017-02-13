package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class InvoiceSearch implements WithCompany {

    @NotNull
    @Nonnull
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
