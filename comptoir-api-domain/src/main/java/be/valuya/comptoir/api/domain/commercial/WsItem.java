package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.common.WithId;
import java.math.BigDecimal;
import java.util.List;
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
@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItem implements WithId {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    private WsItemPictureRef mainPictureRef;
    @NotNull
    @Nonnull
    @Size(max = 128)
    private String reference;
    @NotNull
    @Nonnull
    private List<WsLocaleText> name;
    @NotNull
    @Nonnull
    private List<WsLocaleText> description;
    @NotNull
    @Nonnull
    private BigDecimal vatExclusive;
    @NotNull
    @Nonnull
    private BigDecimal vatRate;

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

    public WsItemPictureRef getMainPictureRef() {
        return mainPictureRef;
    }

    public void setMainPictureRef(WsItemPictureRef mainPictureRef) {
        this.mainPictureRef = mainPictureRef;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<WsLocaleText> getName() {
        return name;
    }

    public void setName(List<WsLocaleText> name) {
        this.name = name;
    }

    public List<WsLocaleText> getDescription() {
        return description;
    }

    public void setDescription(List<WsLocaleText> description) {
        this.description = description;
    }

    public BigDecimal getVatExclusive() {
        return vatExclusive;
    }

    public void setVatExclusive(BigDecimal vatExclusive) {
        this.vatExclusive = vatExclusive;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsItem other = (WsItem) obj;
        return Objects.equals(this.id, other.id);
    }

}
