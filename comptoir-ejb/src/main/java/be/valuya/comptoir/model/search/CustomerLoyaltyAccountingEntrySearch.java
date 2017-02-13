package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Created by cghislai on 24/03/16.
 */
public class CustomerLoyaltyAccountingEntrySearch implements WithCompany {
    @NotNull
    @Nonnull
    private Company company;
    private Customer customer;
    private Sale sale;
    private ZonedDateTime fromDateTime;
    private ZonedDateTime toDateTime;

    @Nonnull
    public Company getCompany() {
        return company;
    }

    public void setCompany(@Nonnull Company company) {
        this.company = company;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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
