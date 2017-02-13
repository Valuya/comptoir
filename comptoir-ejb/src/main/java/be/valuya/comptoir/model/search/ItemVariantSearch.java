package be.valuya.comptoir.model.search;

import be.valuya.comptoir.model.commercial.Item;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cghislai
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemVariantSearch {

    @NotNull
    @Nonnull
    private ItemSearch itemSearch;
    private Item item;
    private String variantReference;
    private String variantReferenceContains;

    public ItemSearch getItemSearch() {
        return itemSearch;
    }

    public void setItemSearch(ItemSearch itemSearch) {
        this.itemSearch = itemSearch;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getVariantReference() {
        return variantReference;
    }

    public void setVariantReference(String variantReference) {
        this.variantReference = variantReference;
    }

    public String getVariantReferenceContains() {
        return variantReferenceContains;
    }

    public void setVariantReferenceContains(String variantReferenceContains) {
        this.variantReferenceContains = variantReferenceContains;
    }

}
