package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.util.WithId;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "Account")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsInvoice implements Serializable, WithId {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    @Size(max = 255)
    private String number;
    private String note;
    @NotNull
    @Nonnull
    private WsSaleRef saleRef;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsInvoice other = (WsInvoice) obj;
        return Objects.equals(this.id, other.id);
    }

}
