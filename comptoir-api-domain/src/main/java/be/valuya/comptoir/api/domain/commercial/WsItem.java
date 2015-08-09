package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.domain.company.WithId;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import java.math.BigDecimal;
import java.util.List;
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
    @Size(max = 128)
    private String model;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

}
