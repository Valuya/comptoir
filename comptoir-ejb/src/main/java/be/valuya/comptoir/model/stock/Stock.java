package be.valuya.comptoir.model.stock;

import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Stock implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    private LocaleText description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocaleText getDescription() {
        return description;
    }

    public void setDescription(LocaleText description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Stock other = (Stock) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
