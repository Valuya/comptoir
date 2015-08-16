package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;
import java.time.ZonedDateTime;
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
public class BalanceSearch {

    @NotNull
    @Nonnull
    private Company company;
    private AccountSearch accountSearch;
    private ZonedDateTime fromDateTime;
    private ZonedDateTime toDateTime;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
}
