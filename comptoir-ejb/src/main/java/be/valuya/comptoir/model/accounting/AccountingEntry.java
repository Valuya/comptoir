package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.misc.LocaleText;
import be.valuya.comptoir.model.commercial.Sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "accounting_entry")
public class AccountingEntry implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    private BigDecimal vatExclusiveAmount;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @ManyToOne
    private LocaleText description;
    @OneToOne
    private Sale sale;

}
