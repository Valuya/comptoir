package be.valuya.comptoir.api.domain.accounting;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.accounting.AccountType;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccount implements Serializable {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    @Size(max = 32)
    private String accountingNumber;
    @Size(max = 32)
    private String iban;
    @Size(max = 12)
    private String bic;
    private String name;
    private WsLocaleText description;
    @NotNull
    @Nonnull
    private AccountType accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WsLocaleText getDescription() {
        return description;
    }

    public void setDescription(WsLocaleText description) {
        this.description = description;
    }

    public String getAccountingNumber() {
        return accountingNumber;
    }

    public void setAccountingNumber(String accountingNumber) {
        this.accountingNumber = accountingNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    @NotNull
    @Nonnull
    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(@NotNull
            @Nonnull AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsAccount other = (WsAccount) obj;
        return Objects.equals(this.id, other.id);
    }

}
