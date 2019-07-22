/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 * @author cghislai
 */
public class StockSearch implements WithCompany {

    @NotNull
    @Nonnull
    private Company company;
    private Boolean active;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
