package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cghislai on 23/03/16.
 */
@XmlRootElement(name = "wsCustomerSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsCustomerSearch {
    @Nonnull
    private WsCompanyRef companyRef;
    private String multiSearch;
    private String lastNameContains;
    private String firstNameContains;
    private String cityContains;
    private String emailContains;
    private String notesContains;

    @Nonnull
    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(@Nonnull WsCompanyRef companyRef) {
        this.companyRef = companyRef;
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
