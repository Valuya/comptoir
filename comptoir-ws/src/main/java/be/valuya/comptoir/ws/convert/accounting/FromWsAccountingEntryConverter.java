package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsCustomerConverter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountingEntryConverter {

    @EJB
    private AccountService accountService;
    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsCustomerConverter fromWsCustomerConverter;
    @Inject
    private FromWsAccountConverter fromWsAccountConverter;
    @Inject
    private FromWsAccountingTransactionConverter fromWsAccountingTransactionConverter;

    public AccountingEntry convert(WsAccountingEntry wsAccountingEntry) {
        if (wsAccountingEntry == null) {
            return null;
        }
        Long id = wsAccountingEntry.getId();
        List<WsLocaleText> description = wsAccountingEntry.getDescription();
        BigDecimal amount = wsAccountingEntry.getAmount();
        ZonedDateTime dateTime = wsAccountingEntry.getDateTime();
        BigDecimal vatRate = wsAccountingEntry.getVatRate();

        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);

        WsCompanyRef companyRef = wsAccountingEntry.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsAccountRef accountRef = wsAccountingEntry.getAccountRef();
        Account account = fromWsAccountConverter.find(accountRef);

        WsAccountingTransactionRef accountingTransactionRef = wsAccountingEntry.getAccountingTransactionRef();
        AccountingTransaction accountingTransaction = fromWsAccountingTransactionConverter.find(accountingTransactionRef);

        WsCustomerRef customerRef = wsAccountingEntry.getCustomerRef();
        Customer customer = fromWsCustomerConverter.find(customerRef);

        WsAccountingEntryRef vatAccountingEntryRef = wsAccountingEntry.getVatAccountingEntryRef();
        AccountingEntry vatAccountingEntry = find(vatAccountingEntryRef);

        AccountingEntry accountingEntry = new AccountingEntry();
        accountingEntry.setId(id);
        accountingEntry.setDescription(wsDescription);
        accountingEntry.setCompany(company);
        accountingEntry.setAccount(account);
        accountingEntry.setAccountingTransaction(accountingTransaction);
        accountingEntry.setAmount(amount);
        accountingEntry.setCustomer(customer);
        accountingEntry.setDateTime(dateTime);
        accountingEntry.setVatAccountingEntry(vatAccountingEntry);
        accountingEntry.setVatRate(vatRate);

        return accountingEntry;
    }

    public AccountingEntry find(WsAccountingEntryRef accountingEntryRef) {
        if (accountingEntryRef == null) {
            return null;
        }
        Long id = accountingEntryRef.getId();
        return accountService.findAccountingEntryById(id);
    }

}
