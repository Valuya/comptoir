package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
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

}
