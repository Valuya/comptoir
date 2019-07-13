package be.valuya.comptoir.persistence.util;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.Account_;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.Balance_;
import be.valuya.comptoir.util.pagination.BalanceColumn;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class BalanceColumnPersistenceUtil {

    public static Path<?> getPath(From<?, Balance> balanceFrom, BalanceColumn balanceColumn) {
        switch (balanceColumn) {
            case ACCOUNT:
                Path<Account> accountPath = balanceFrom.get(Balance_.account);
                return accountPath.get(Account_.name);
            case DATETIME:
                return balanceFrom.get(Balance_.dateTime);
            case BALANCE:
                return balanceFrom.get(Balance_.balance);
            case COMMENT:
                return balanceFrom.get(Balance_.comment);
            case CLOSED:
                return balanceFrom.get(Balance_.closed);

            default:
                throw new AssertionError(balanceColumn.name());
        }
    }

}
