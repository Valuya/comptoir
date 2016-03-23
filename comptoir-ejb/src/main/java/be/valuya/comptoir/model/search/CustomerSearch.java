package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cghislai on 23/03/16.
 */
public class CustomerSearch {
    @NotNull
    @Nonnull
    private Company company;
    private String multiSearch;
    private String lastNameContains;
    private String firstNameContains;
    private String cityContains;
    private String emailContains;
    private String notesContains;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(String multiSearch) {
        this.multiSearch = multiSearch;
    }

    public String getLastNameContains() {
        return lastNameContains;
    }

    public void setLastNameContains(String lastNameContains) {
        this.lastNameContains = lastNameContains;
    }

    public String getFirstNameContains() {
        return firstNameContains;
    }

    public void setFirstNameContains(String firstNameContains) {
        this.firstNameContains = firstNameContains;
    }

    public String getCityContains() {
        return cityContains;
    }

    public void setCityContains(String cityContains) {
        this.cityContains = cityContains;
    }

    public String getEmailContains() {
        return emailContains;
    }

    public void setEmailContains(String emailContains) {
        this.emailContains = emailContains;
    }

    public String getNotesContains() {
        return notesContains;
    }

    public void setNotesContains(String notesContains) {
        this.notesContains = notesContains;
    }
}
