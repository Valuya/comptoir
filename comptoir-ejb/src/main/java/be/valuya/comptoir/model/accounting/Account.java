package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    private Company company;
    @Column(name = "accounting_number", length = 32)
    @Size(max = 32)
    private String accountingNumber;
    @Column(length = 32)
    @Size(max = 32)
    private String iban;
    @Column(length = 12)
    @Size(max = 12)
    private String bic;
    private String name;
    @ManyToOne
    private LocaleText description;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Nonnull
    private AccountType accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocaleText getDescription() {
        return description;
    }

    public void setDescription(LocaleText description) {
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
        final Account other = (Account) obj;
        return Objects.equals(this.id, other.id);
    }

}
