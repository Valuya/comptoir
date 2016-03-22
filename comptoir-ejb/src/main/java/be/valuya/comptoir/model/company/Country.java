package be.valuya.comptoir.model.company;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cghislai on 22/03/16.
 */
@Entity
@Table(name="country")
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Nonnull
    private String code;
    @Column(name = "DEFAULT_VAT_RATE")
    @NotNull
    @Nonnull
    private BigDecimal defaultVatRate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDefaultVatRate() {
        return defaultVatRate;
    }

    public void setDefaultVatRate(BigDecimal defaultVatRate) {
        this.defaultVatRate = defaultVatRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return id != null ? id.equals(country.id) : country.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
