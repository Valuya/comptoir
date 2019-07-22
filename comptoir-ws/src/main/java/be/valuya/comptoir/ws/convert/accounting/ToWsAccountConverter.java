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
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Yannick Majorimport be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
 * import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
 * os <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private ToWsAccountTypeConverter toWsAccountTypeConverter;

    public WsAccount convert(Account account) {
        if (account == null) {
            return null;
        }
        Long id = account.getId();
        AccountType accountType = account.getAccountType();
        String accountingNumber = account.getAccountingNumber();
        String bic = account.getBic();
        Company company = account.getCompany();
        LocaleText description = account.getDescription();
        String iban = account.getIban();
        String name = account.getName();
        boolean cash = account.isCash();

        List<WsLocaleText> wsDescription = fromWsLocaleTextConverter.convert(description);
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);
        WsAccountType wsAccountType = toWsAccountTypeConverter.toWsAccountType(accountType);

        WsAccount wsAccount = new WsAccount();
        wsAccount.setId(id);
        wsAccount.setDescription(wsDescription);
        wsAccount.setAccountType(wsAccountType);
        wsAccount.setAccountingNumber(accountingNumber);
        wsAccount.setBic(bic);
        wsAccount.setCompanyRef(companyRef);
        wsAccount.setBic(bic);
        wsAccount.setIban(iban);
        wsAccount.setName(name);
        wsAccount.setCash(cash);

        return wsAccount;
    }

    public WsAccountRef reference(Account account) {
        Long id = account.getId();
        WsAccountRef wsAccountRef = new WsAccountRef(id);
        return wsAccountRef;
    }

}
