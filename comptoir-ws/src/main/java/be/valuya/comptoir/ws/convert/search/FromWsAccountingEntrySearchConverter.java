package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsAccountingEntrySearch;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.AccountSearch;
import be.valuya.comptoir.model.search.AccountingEntrySearch;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountingTransactionConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountingEntrySearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsAccountingTransactionConverter fromWsAccountingTransactionConverter;
    @Inject
    private FromWsAccountSearchConverter fromWsAccountSearchConverter;

    public AccountingEntrySearch convert(WsAccountingEntrySearch wsAccountingEntrySearch) {
        if (wsAccountingEntrySearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsAccountingEntrySearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsAccountingTransactionRef accountingTransactionRef = wsAccountingEntrySearch.getAccountingTransactionRef();
        AccountingTransaction accountingTransaction = fromWsAccountingTransactionConverter.find(accountingTransactionRef);

        AccountSearch accountSearch = wsAccountingEntrySearch.getAccountSearch();
        ZonedDateTime fromDateTime = wsAccountingEntrySearch.getFromDateTime();
        ZonedDateTime toDateTime = wsAccountingEntrySearch.getToDateTime();

        AccountingEntrySearch accountingEntrySearch = new AccountingEntrySearch();
        accountingEntrySearch.setAccountSearch(accountSearch);
        accountingEntrySearch.setCompany(company);
        accountingEntrySearch.setAccountingTransaction(accountingTransaction);
        accountingEntrySearch.setFromDateTime(fromDateTime);
        accountingEntrySearch.setToDateTime(toDateTime);

        return accountingEntrySearch;
    }

}
