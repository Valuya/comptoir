package be.valuya.comptoir.api.domain.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "SaleSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsSaleSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;
    private Boolean closed;
    @CheckForNull
    private WsLocaleSearch localeSearch;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @CheckForNull
    public WsLocaleSearch getLocaleSearch() {
        return localeSearch;
    }

    public void setLocaleSearch(WsLocaleSearch localeSearch) {
        this.localeSearch = localeSearch;
    }
    

}
