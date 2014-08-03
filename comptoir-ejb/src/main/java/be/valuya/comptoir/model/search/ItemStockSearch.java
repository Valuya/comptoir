package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.stock.Stock;
import java.time.ZonedDateTime;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemStockSearch {

    @NotNull
    @Nonnull
    private Company company;
    @CheckForNull
    private Item item;
    @CheckForNull
    private Stock stock;
    @CheckForNull
    private ZonedDateTime atDateTime;

    @NotNull
    @Nonnull
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @CheckForNull
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @CheckForNull
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @CheckForNull
    public ZonedDateTime getAtDateTime() {
        return atDateTime;
    }

    public void setAtDateTime(ZonedDateTime atDateTime) {
        this.atDateTime = atDateTime;
    }

}