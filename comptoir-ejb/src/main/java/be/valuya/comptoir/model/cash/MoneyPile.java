package be.valuya.comptoir.model.cash;

import be.valuya.comptoir.model.accounting.Account;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Counted pile of money (e.g.: 53 x 0.10 € = 5.3 €) that can be part of a balance.
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "money_pile")
public class MoneyPile implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Account account;
    @Column(name = "date_time")
    private ZonedDateTime dateTime;
    @Column(name = "unit_amount")
    private BigDecimal unitAmount;
    @Column(name = "unit_count")
    private BigDecimal count;
    private BigDecimal total;
    @ManyToOne
    private Balance balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

}
