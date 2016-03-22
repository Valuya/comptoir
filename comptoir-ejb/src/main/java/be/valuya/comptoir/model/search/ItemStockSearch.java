package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemStockSearch {

    @NotNull
    @Nonnull
    private Company company;
    @CheckForNull
    private ItemVariant itemVariant;
    @CheckForNull
    private Stock stock;
    @CheckForNull
    private ZonedDateTime atDateTime;
    @CheckForNull
    private ZonedDateTime fromDateTime;
    @CheckForNull
    private Sale sale;
    @CheckForNull
    private ItemStock previousItemStock;

    @NotNull
    @Nonnull
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @CheckForNull
    public ItemVariant getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(ItemVariant itemVariant) {
        this.itemVariant = itemVariant;
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

    @CheckForNull

    public ZonedDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(ZonedDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    @CheckForNull
    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public ItemStock getPreviousItemStock() {
        return previousItemStock;
    }

    public void setPreviousItemStock(ItemStock previousItemStock) {
        this.previousItemStock = previousItemStock;
    }
}
