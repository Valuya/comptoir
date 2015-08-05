package be.valuya.comptoir.comptoir.ws.convert.accounting;

import be.valuya.comptoir.api.domain.accounting.WsAccount;
import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.comptoir.ws.ToWsLocaleTextConverter;
import be.valuya.comptoir.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsAccount convert(Account account) {
        Long id = account.getId();
        AccountType accountType = account.getAccountType();
        String accountingNumber = account.getAccountingNumber();
        String bic = account.getBic();
        Company company = account.getCompany();
        LocaleText description = account.getDescription();
        String iban = account.getIban();
        String name = account.getName();

        WsLocaleText wsDescription = fromWsLocaleTextConverter.convert(description);
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsAccount wsAccount = new WsAccount();
        wsAccount.setId(id);
        wsAccount.setDescription(wsDescription);
        wsAccount.setAccountType(accountType);
        wsAccount.setAccountingNumber(accountingNumber);
        wsAccount.setBic(bic);
        wsAccount.setCompanyRef(companyRef);
        wsAccount.setBic(bic);
        wsAccount.setIban(iban);
        wsAccount.setName(name);

        return wsAccount;
    }

    public WsAccountRef reference(Account account) {
        Long id = account.getId();
        WsAccountRef wsAccountRef = new WsAccountRef(id);
        return wsAccountRef;
    }

}
