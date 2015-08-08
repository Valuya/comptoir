package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccountingTransaction;
import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.accounting.AccountingTransactionType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.AccountService;
import java.time.ZonedDateTime;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountingTransactionConverter {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public AccountingTransaction convert(WsAccountingTransaction wsAccountingTransaction) {
        Long id = wsAccountingTransaction.getId();
        WsCompanyRef companyRef = wsAccountingTransaction.getCompanyRef();
        AccountingTransactionType accountingTransactionType = wsAccountingTransaction.getAccountingTransactionType();
        ZonedDateTime dateTime = wsAccountingTransaction.getDateTime();

        Company company = fromWsCompanyConverter.find(companyRef);

        AccountingTransaction accountingTransaction = new AccountingTransaction();
        accountingTransaction.setId(id);
        accountingTransaction.setCompany(company);
        accountingTransaction.setAccountingTransactionType(accountingTransactionType);
        accountingTransaction.setDateTime(dateTime);

        return accountingTransaction;
    }

    public AccountingTransaction find(WsAccountingTransactionRef accountingTransactionRef) {
        Long id = accountingTransactionRef.getId();
        return accountService.findAccountingTransactionById(id);
    }

}
