package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Nullable
public class ItemVariantSaleSearch implements WithCompany {

    @NotNull
    @Nonnull
    private Company company;
    private ItemVariant itemVariant;
    private Sale sale;
    private SaleSearch saleSearch;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ItemVariant getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(ItemVariant itemVariant) {
        this.itemVariant = itemVariant;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public SaleSearch getSaleSearch() {
        return saleSearch;
    }

    public void setSaleSearch(SaleSearch saleSearch) {
        this.saleSearch = saleSearch;
    }
}
