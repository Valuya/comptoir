package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantSearch {

    @Nonnull
    private WsCompanyRef companyRef;
    private WsItemRef itemRef;
    private String nameContains;
    private String descriptionContains;
    private String reference;
    private String referenceContains;
    private String variantReference;
    private String variantReferenceContains;

    private String multiSearch;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
    }

    public String getNameContains() {
        return nameContains;
    }

    public void setNameContains(String nameContains) {
        this.nameContains = nameContains;
    }

    public String getDescriptionContains() {
        return descriptionContains;
    }

    public void setDescriptionContains(String descriptionContains) {
        this.descriptionContains = descriptionContains;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceContains() {
        return referenceContains;
    }

    public void setReferenceContains(String referenceContains) {
        this.referenceContains = referenceContains;
    }

    public String getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(String multiSearch) {
        this.multiSearch = multiSearch;
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