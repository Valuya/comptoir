package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemVariantSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantSearch {

    @Nonnull
    private WsItemSearch itemSearch;
    private WsItemRef itemRef;
    private String variantReference;
    private String variantReferenceContains;

    public WsItemSearch getItemSearch() {
        return itemSearch;
    }

    public void setItemSearch(WsItemSearch itemSearch) {
        this.itemSearch = itemSearch;
    }

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
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
