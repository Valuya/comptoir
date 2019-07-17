package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class ToWsAccountingTransactionTypeConverter {

    public WsAccountingTransactionType toWsAccountingTransactionType(AccountingTransactionType transactionType) {
        switch (transactionType) {
            case SALE:
                return WsAccountingTransactionType.SALE;
            case PURCHASE:
                return WsAccountingTransactionType.PURCHASE;
            case TRANSFER:
                return WsAccountingTransactionType.TRANSFER;
            default:
                throw new IllegalArgumentException(Objects.toString(transactionType));
        }
    }
}
