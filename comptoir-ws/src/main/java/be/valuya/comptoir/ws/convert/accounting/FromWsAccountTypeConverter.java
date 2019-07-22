package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountTypeConverter {


    public AccountType fromWsAccountType(WsAccountType wsAccountType) {
        switch (wsAccountType) {
            case VAT:
                return AccountType.VAT;
            case OTHER:
                return AccountType.OTHER;
            case PAYMENT:
                return AccountType.PAYMENT;
            default:
                throw new IllegalArgumentException(Objects.toString(wsAccountType));
        }
    }


}
