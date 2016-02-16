package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.company.Company;
import java.time.ZonedDateTime;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class SaleSearch {

    @NotNull
    @Nonnull
    private Company company;
    private Boolean closed;
    private ZonedDateTime fromDateTime;
    private ZonedDateTime toDateTime;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
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
