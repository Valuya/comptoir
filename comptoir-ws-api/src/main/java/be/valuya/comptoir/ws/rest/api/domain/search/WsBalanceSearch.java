package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;
import java.time.ZonedDateTime;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "AccountingEntrySearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsBalanceSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;
    private WsAccountSearch accountSearch;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime fromDateTime;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime toDateTime;
    private WsAccountRef accountRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public WsAccountSearch getAccountSearch() {
        return accountSearch;
    }

    public void setAccountSearch(WsAccountSearch accountSearch) {
        this.accountSearch = accountSearch;
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

    public WsAccountRef getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(WsAccountRef accountRef) {
        this.accountRef = accountRef;
    }

}
