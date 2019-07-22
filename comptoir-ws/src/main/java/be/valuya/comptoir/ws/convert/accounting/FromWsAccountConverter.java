package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccount;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountType;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;

import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsAccountConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsAccountTypeConverter fromWsAccountTypeConverter;
    @EJB
    private AccountService accountService;

    public Account convert(WsAccount wsAccount) {
        if (wsAccount == null) {
            return null;
        }
        Long id = wsAccount.getId();
        WsAccountType wsAccountType = wsAccount.getAccountType();
        String accountingNumber = wsAccount.getAccountingNumber();
        String bic = wsAccount.getBic();
        WsCompanyRef companyRef = wsAccount.getCompanyRef();
        List<WsLocaleText> description = wsAccount.getDescription();
        String iban = wsAccount.getIban();
        String name = wsAccount.getName();

        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);
        Company company = fromWsCompanyConverter.find(companyRef);
        AccountType accountType = fromWsAccountTypeConverter.fromWsAccountType(wsAccountType);

        boolean cash = wsAccount.isCash();

        Account account = new Account();
        account.setId(id);
        account.setDescription(wsDescription);
        account.setAccountType(accountType);
        account.setAccountingNumber(accountingNumber);
        account.setBic(bic);
        account.setCompany(company);
        account.setBic(bic);
        account.setIban(iban);
        account.setName(name);
        account.setCash(cash);

        return account;
    }


    public Account find(WsAccountRef accountRef) {
        if (accountRef == null) {
            return null;
        }
        Long id = accountRef.getId();
        return accountService.findAccountById(id);
    }

}
