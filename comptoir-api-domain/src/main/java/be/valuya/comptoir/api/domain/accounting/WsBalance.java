package be.valuya.comptoir.api.domain.accounting;

import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "Balance")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsBalance implements Serializable, WithId {

    private Long id;
    private WsAccountRef accountRef;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime dateTime;
    private BigDecimal balance;
    private String comment;
    private boolean closed;

    @Override
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
