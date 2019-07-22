package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.util.LocaleXmlAdapter;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
    @XmlJavaTypeAdapter(LocaleXmlAdapter.class)
    private Locale locale;

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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
