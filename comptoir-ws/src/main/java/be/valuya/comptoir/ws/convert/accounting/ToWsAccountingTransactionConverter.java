package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransaction;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionType;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;

import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountingTransactionConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private ToWsAccountingTransactionTypeConverter toWsAccountingTransactionTypeConverter;

    public WsAccountingTransaction convert(AccountingTransaction accountingTransaction) {
        if (accountingTransaction == null) {
            return null;
        }
        Long id = accountingTransaction.getId();
        Company company = accountingTransaction.getCompany();
        AccountingTransactionType accountingTransactionType = accountingTransaction.getAccountingTransactionType();
        ZonedDateTime dateTime = accountingTransaction.getDateTime();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);
        WsAccountingTransactionType wsAccountingTransactionType = toWsAccountingTransactionTypeConverter.toWsAccountingTransactionType(accountingTransactionType);

        WsAccountingTransaction wsaccountingTransaction = new WsAccountingTransaction();
        wsaccountingTransaction.setId(id);
        wsaccountingTransaction.setCompanyRef(companyRef);
        wsaccountingTransaction.setWsAccountingTransactionType(wsAccountingTransactionType);
        wsaccountingTransaction.setDateTime(dateTime);

        return wsaccountingTransaction;
    }

    public WsAccountingTransactionRef reference(AccountingTransaction accountingTransaction) {
        Long id = accountingTransaction.getId();
        WsAccountingTransactionRef accountingTransactionRef = new WsAccountingTransactionRef(id);
        return accountingTransactionRef;
    }

}
