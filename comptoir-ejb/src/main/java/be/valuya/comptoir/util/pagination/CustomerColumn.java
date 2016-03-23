package be.valuya.comptoir.util.pagination;

import be.valuya.comptoir.model.thirdparty.Customer;

/**
 * Created by cghislai on 23/03/16.
 */
public enum CustomerColumn implements Column<Customer> {
    ID,
    FIRST_NAME,
    LAST_NAME,
    ADDRESS1,
    ADDRESS2,
    ZIP,
    CITY,
    PHONE1,
    PHONE2,
    EMAIL,
    NOTES
}
