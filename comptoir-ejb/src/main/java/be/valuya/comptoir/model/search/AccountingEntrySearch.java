package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import java.time.ZonedDateTime;
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
public class AccountingEntrySearch {

    @NotNull
    @Nonnull
    private Company company;
    private AccountingTransaction accountingTransaction;
    private AccountSearch accountSearch;
    private ZonedDateTime fromDateTime;
    private ZonedDateTime toDateTime;
    @CheckForNull
    private LocaleSearch localeSearch;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccountingTransaction getAccountingTransaction() {
        return accountingTransaction;
    }

    public void setAccountingTransaction(AccountingTransaction accountingTransaction) {
        this.accountingTransaction = accountingTransaction;
    }

    public AccountSearch getAccountSearch() {
        return accountSearch;
    }

    public void setAccountSearch(AccountSearch accountSearch) {
        this.accountSearch = accountSearch;
    }

    public ZonedDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(ZonedDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public ZonedDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(ZonedDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    @CheckForNull
    public LocaleSearch getLocaleSearch() {
        return localeSearch;
    }

    public void setLocaleSearch(LocaleSearch localeSearch) {
        this.localeSearch = localeSearch;
    }

}
