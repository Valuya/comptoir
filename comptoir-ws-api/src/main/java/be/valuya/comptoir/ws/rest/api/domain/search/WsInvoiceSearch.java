package be.valuya.comptoir.ws.rest.api.domain.search;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "InvoiceSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsInvoiceSearch {

    @Nonnull
    @NotNull
    private WsCompanyRef companyRef;

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

}
