/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.util.pagination;

import be.valuya.comptoir.model.accounting.Account;

/**
 *
 * @author cghislai
 */
public enum AccountColumn implements Column<Account> {

    ACCOUNTING_NUMBER,
    IBAN,
    BIC,
    NAME,
    DESCRIPTION,
    ACCOUNT_TYPE
}
