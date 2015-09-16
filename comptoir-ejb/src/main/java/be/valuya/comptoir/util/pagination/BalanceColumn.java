/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.util.pagination;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.cash.Balance;

/**
 *
 * @author cghislai
 */
public enum BalanceColumn implements Column<Balance> {

    ACCOUNT,
    DATETIME,
    BALANCE,
    COMMENT,
    CLOSED
}
