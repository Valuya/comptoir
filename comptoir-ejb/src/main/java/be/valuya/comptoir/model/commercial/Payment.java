package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.Account;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class Payment implements Serializable {

    private BigDecimal amount;
    private Account account;

    public Payment() {
    }

    public Payment(BigDecimal amount, Account account) {
        this.amount = amount;
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
