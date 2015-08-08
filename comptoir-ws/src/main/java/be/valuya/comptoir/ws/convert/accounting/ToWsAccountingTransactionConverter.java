package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccountingTransaction;
import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.company.Company;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountingTransactionConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsAccountingTransaction convert(AccountingTransaction accountingTransaction) {
        Long id = accountingTransaction.getId();
        Company company = accountingTransaction.getCompany();
        AccountingTransactionType accountingTransactionType = accountingTransaction.getAccountingTransactionType();
        ZonedDateTime dateTime = accountingTransaction.getDateTime();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsAccountingTransaction wsaccountingTransaction = new WsAccountingTransaction();
        wsaccountingTransaction.setId(id);
        wsaccountingTransaction.setCompanyRef(companyRef);
        wsaccountingTransaction.setAccountingTransactionType(accountingTransactionType);
        wsaccountingTransaction.setDateTime(dateTime);

        return wsaccountingTransaction;
    }

    public WsAccountingTransactionRef reference(AccountingTransaction accountingTransaction) {
        Long id = accountingTransaction.getId();
        WsAccountingTransactionRef accountingTransactionRef = new WsAccountingTransactionRef(id);
        return accountingTransactionRef;
    }

}
