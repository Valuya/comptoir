package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountType;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AccountSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;
    private WsAccountType accountType;
    private WsPosRef posRef;
    private Boolean cash;
    private String multiSearch;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(WsAccountType accountType) {
        this.accountType = accountType;
    }

    public WsPosRef getPosRef() {
        return posRef;
    }

    public void setPosRef(WsPosRef posRef) {
        this.posRef = posRef;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public String getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(String multiSearch) {
        this.multiSearch = multiSearch;
    }
}
