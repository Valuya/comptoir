package be.valuya.comptoir.ws.convert.accounting;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccount;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountType;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAccountTypeConverter {

    public WsAccountType toWsAccountType(AccountType accountType) {
        switch (accountType) {
            case VAT:
                return WsAccountType.VAT;
            case OTHER:
                return WsAccountType.OTHER;
            case PAYMENT:
                return WsAccountType.PAYMENT;
            default:
                throw new IllegalArgumentException(Objects.toString(accountType));
        }
    }


}
