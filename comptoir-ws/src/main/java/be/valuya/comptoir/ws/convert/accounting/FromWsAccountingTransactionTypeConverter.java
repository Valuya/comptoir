package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class FromWsAccountingTransactionTypeConverter {

    public AccountingTransactionType fromWsAccountingTransactionType(WsAccountingTransactionType wsAccountingTransactionType) {
        switch (wsAccountingTransactionType) {

            case SALE:
                return AccountingTransactionType.SALE;
            case PURCHASE:
                return AccountingTransactionType.PURCHASE;
            case TRANSFER:
                return AccountingTransactionType.TRANSFER;
            default:
                throw new IllegalArgumentException(Objects.toString(wsAccountingTransactionType));
        }
    }
}
