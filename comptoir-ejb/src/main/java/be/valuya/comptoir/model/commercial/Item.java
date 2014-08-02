package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Item implements Serializable {

    @Id
    private Long id;
    @Column(length = 128)
    @Size(max = 128)
    private String barCode;
    private LocaleText description;
    @ManyToOne
    @JoinColumn(name = "current_price_id")
    private ItemPrice currentPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public LocaleText getDescription() {
        return description;
    }

    public void setDescription(LocaleText description) {
        this.description = description;
    }

    public ItemPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ItemPrice currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Item other = (Item) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
