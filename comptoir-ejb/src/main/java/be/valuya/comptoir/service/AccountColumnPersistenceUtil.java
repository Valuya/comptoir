package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.util.pagination.AccountColumn;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class AccountColumnPersistenceUtil {

    public static Path<?> getPath(From<?, Account> accountFrom, AccountColumn accountColumn) {
        switch (accountColumn) {
            case ACCOUNTING_NUMBER:
                return accountFrom.get(Account_.accountingNumber);
            case IBAN:
                return accountFrom.get(Account_.iban);
            case BIC:
                return accountFrom.get(Account_.bic);
            case NAME:
                return accountFrom.get(Account_.name);
            case DESCRIPTION:
                return accountFrom.get(Account_.description);
            case ACCOUNT_TYPE:
                return accountFrom.get(Account_.accountType);
            default:
                throw new AssertionError(accountColumn.name());
        }
    }

}
