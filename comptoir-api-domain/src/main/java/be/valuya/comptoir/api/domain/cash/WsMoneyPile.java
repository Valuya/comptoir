package be.valuya.comptoir.api.domain.cash;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.api.domain.company.WithId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Counted pile of money (e.g.: 53 x 0.10 € = 5.3 €) that can be part of a balance.
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "MoneyPile")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsMoneyPile implements Serializable, WithId {

    private Long id;
    private WsAccountRef accountRef;
    private ZonedDateTime dateTime;
    private BigDecimal unitAmount;
    private BigDecimal count;
    private BigDecimal total;
    private WsBalanceRef balanceRef;

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

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public WsBalanceRef getBalanceRef() {
        return balanceRef;
    }

    public void setBalanceRef(WsBalanceRef balanceRef) {
        this.balanceRef = balanceRef;
    }
}
