package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransaction;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionType;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;

import java.time.ZonedDateTime;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountingTransactionConverter {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsAccountingTransactionTypeConverter fromWsAccountingTransactionTypeConverter;

    public AccountingTransaction convert(WsAccountingTransaction wsAccountingTransaction) {
        if (wsAccountingTransaction == null) {
            return null;
        }
        Long id = wsAccountingTransaction.getId();
        WsCompanyRef companyRef = wsAccountingTransaction.getCompanyRef();
        WsAccountingTransactionType wsAccountingTransactionType = wsAccountingTransaction.getWsAccountingTransactionType();
        ZonedDateTime dateTime = wsAccountingTransaction.getDateTime();

        Company company = fromWsCompanyConverter.find(companyRef);
        AccountingTransactionType accountingTransactionType = fromWsAccountingTransactionTypeConverter.fromWsAccountingTransactionType(wsAccountingTransactionType);

        AccountingTransaction accountingTransaction = new AccountingTransaction();
        accountingTransaction.setId(id);
        accountingTransaction.setCompany(company);
        accountingTransaction.setAccountingTransactionType(accountingTransactionType);
        accountingTransaction.setDateTime(dateTime);

        return accountingTransaction;
    }

    public AccountingTransaction find(WsAccountingTransactionRef accountingTransactionRef) {
        if (accountingTransactionRef == null) {
            return null;
        }
        Long id = accountingTransactionRef.getId();
        return accountService.findAccountingTransactionById(id);
    }

}
