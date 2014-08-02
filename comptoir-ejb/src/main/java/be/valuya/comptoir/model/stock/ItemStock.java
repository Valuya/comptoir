package be.valuya.comptoir.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_stock")
public class ItemStock implements Serializable {

    @Id
    private Long id;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    @JoinColumn(name = "previous_item_stock_id")
    private ItemStock previousItemStock;
    private BigDecimal quantity;

}
