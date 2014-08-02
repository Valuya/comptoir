package be.valuya.comptoir.model.stock;

import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
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
}
