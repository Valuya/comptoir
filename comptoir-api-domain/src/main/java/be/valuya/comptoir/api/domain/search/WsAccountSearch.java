package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.accounting.AccountType;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AccountSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;
    private AccountType accountType;
    private WsPosRef posRef;
    private Boolean cash;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
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

}
