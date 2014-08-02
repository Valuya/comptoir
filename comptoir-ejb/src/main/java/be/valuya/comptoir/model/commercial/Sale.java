package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Sale implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    private Customer customer;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @OneToOne(mappedBy = "sale", fetch = FetchType.LAZY)
    private Invoice invoice;
    @OneToOne
    private AccountingEntry accountingEntry;

}
