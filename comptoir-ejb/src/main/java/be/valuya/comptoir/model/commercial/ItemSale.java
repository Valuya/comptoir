package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "item_sale")
public class ItemSale implements Serializable {

    @Id
    private Long id;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @ManyToOne
    private Item item;
    @ManyToOne
    private ItemPrice price;
    @ManyToOne
    private Sale sale;
    @ManyToOne
    private LocaleText localeText;
    @OneToOne
    private AccountingEntry accountingEntry;
}
