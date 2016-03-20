package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.stock.Stock;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "pos_stock")
public class PosStock implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    @JoinColumn(name = "pos_id")
    private Pos pointOfSale;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pos getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(Pos pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    @Nonnull
    public Stock getStock() {
        return stock;
    }

    public void setStock(@Nonnull Stock stock) {
        this.stock = stock;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final PosStock other = (PosStock) obj;
        return Objects.equals(this.id, other.id);
    }

}
