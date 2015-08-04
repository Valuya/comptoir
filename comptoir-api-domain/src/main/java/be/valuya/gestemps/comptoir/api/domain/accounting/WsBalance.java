package be.valuya.gestemps.comptoir.api.domain.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "balance")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsBalance implements Serializable {

    private Long id;
    private WsAccountRef accountRef;
    private ZonedDateTime dateTime;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsAccountRef getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(WsAccountRef accountRef) {
        this.accountRef = accountRef;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
