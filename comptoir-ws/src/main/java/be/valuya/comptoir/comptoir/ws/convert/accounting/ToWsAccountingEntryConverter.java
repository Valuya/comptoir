package be.valuya.comptoir.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import be.valuya.comptoir.comptoir.ws.convert.thirdparty.ToWsCustomerConverter;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.thirdparty.Customer;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountingEntryConverter {

    @Inject
    private ToWsLocaleTextConverter toWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private ToWsAccountConverter toWsAccountConverter;
    @Inject
    private ToWsCustomerConverter toWsCustomerConverter;
    @Inject
    private ToWsAccountingTransactionConverter toWsAccountingTransactionConverter;

    public WsAccountingEntry convert(AccountingEntry accountingEntry) {
        Long id = accountingEntry.getId();
        LocaleText description = accountingEntry.getDescription();
        BigDecimal amount = accountingEntry.getAmount();
        ZonedDateTime dateTime = accountingEntry.getDateTime();
        BigDecimal vatRate = accountingEntry.getVatRate();

        WsLocaleText wsDescription = toWsLocaleTextConverter.convert(description);

        Company company = accountingEntry.getCompany();
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        Account account = accountingEntry.getAccount();
        WsAccountRef accountRef = toWsAccountConverter.reference(account);

        AccountingTransaction accountingTransaction = accountingEntry.getAccountingTransaction();
        WsAccountingTransactionRef accountingTransactionRef = toWsAccountingTransactionConverter.reference(accountingTransaction);

        Customer customer = accountingEntry.getCustomer();
        WsCustomerRef customerRef = toWsCustomerConverter.reference(customer);

        AccountingEntry vatAccountingEntry = accountingEntry.getVatAccountingEntry();
        WsAccountingEntryRef vatAccountingEntryRef = reference(vatAccountingEntry);

        WsAccountingEntry wsAccountingEntry = new WsAccountingEntry();
        wsAccountingEntry.setId(id);
        wsAccountingEntry.setDescription(wsDescription);
        wsAccountingEntry.setCompanyRef(companyRef);
        wsAccountingEntry.setAccountRef(accountRef);
        wsAccountingEntry.setAccountingTransactionRef(accountingTransactionRef);
        wsAccountingEntry.setAmount(amount);
        wsAccountingEntry.setCustomerRef(customerRef);
        wsAccountingEntry.setDateTime(dateTime);
        wsAccountingEntry.setVatAccountingEntryRef(vatAccountingEntryRef);
        wsAccountingEntry.setVatRate(vatRate);

        return wsAccountingEntry;
    }

    public WsAccountingEntryRef reference(AccountingEntry accountingEntry) {
        Long id = accountingEntry.getId();
        WsAccountingEntryRef wsAccountRef = new WsAccountingEntryRef(id);
        return wsAccountRef;
    }

}
