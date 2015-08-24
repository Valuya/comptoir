package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.accounting.Account;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "pos_payment_account")
public class PosPaymentAccount implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    @JoinColumn(name = "pos_id")
    private Pos pointOfSale;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    private Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pos getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(Pos pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PosPaymentAccount other = (PosPaymentAccount) obj;
        return Objects.equals(this.id, other.id);
    }

}
