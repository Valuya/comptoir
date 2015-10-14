package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AttributeSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAttributeSearch {

    private String nameContains;
    private String valueContains;
    @Nonnull
    private WsCompanyRef companyRef;
    private String multiSearch;
    @CheckForNull
    private WsLocaleSearch localeSearch;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public String getNameContains() {
        return nameContains;
    }

    public void setNameContains(String nameContains) {
        this.nameContains = nameContains;
    }

    public String getValueContains() {
        return valueContains;
    }

    public void setValueContains(String valueContains) {
        this.valueContains = valueContains;
    }

    public String getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(String multiSearch) {
        this.multiSearch = multiSearch;
    }

    @CheckForNull
    public WsLocaleSearch getLocaleSearch() {
        return localeSearch;
    }

    public void setLocaleSearch(WsLocaleSearch localeSearch) {
        this.localeSearch = localeSearch;
    }

}
