package be.valuya.comptoir.model.commercial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name="item_price")
public class ItemPrice implements Serializable {

    @Id
    private Long id;
    @Column(name = "start")
    private ZonedDateTime startDateTime;
    @Column(name = "end")
    private ZonedDateTime endDateTime;
    private Item item;
    private BigDecimal value;

}
