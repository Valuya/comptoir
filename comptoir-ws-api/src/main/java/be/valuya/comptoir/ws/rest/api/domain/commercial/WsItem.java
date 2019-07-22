package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.ws.rest.api.util.WithId;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(name = "WsItem", description = "Ws Item")
public class WsItem implements WithId, Serializable {

    private Long id;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    private WsPictureRef mainPictureRef;
    @NotNull
    @Nonnull
    @Size(max = 128)
    private String reference;
    @NotNull
    @Nonnull
    @Schema(required = true, type = SchemaType.ARRAY, implementation = WsLocaleText.class)
    private List<WsLocaleText> name;
    @NotNull
    @Nonnull
    @Schema(required = true)
    private List<WsLocaleText> description;
    @NotNull
    @Nonnull
    private BigDecimal vatExclusive;
    @NotNull
    @Nonnull
    private BigDecimal vatRate;
    private boolean multipleSale;

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

    public WsPictureRef getMainPictureRef() {
        return mainPictureRef;
    }

    public void setMainPictureRef(WsPictureRef mainPictureRef) {
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

    public boolean isMultipleSale() {
        return multipleSale;
    }

    public void setMultipleSale(boolean multipleSale) {
        this.multipleSale = multipleSale;
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
