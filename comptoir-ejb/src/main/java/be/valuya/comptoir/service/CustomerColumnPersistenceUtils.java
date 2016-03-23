package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.*;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.model.thirdparty.Customer_;
import be.valuya.comptoir.util.pagination.CustomerColumn;
import be.valuya.comptoir.util.pagination.ItemVariantSaleColumn;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 * Created by cghislai on 23/03/16.
 */
public class CustomerColumnPersistenceUtils {

    public static Path<?> getPath(From<?, Customer> customerFrom, CustomerColumn customerColumn) {
       switch (customerColumn) {
           case ID:
               return customerFrom.get(Customer_.id);
           case FIRST_NAME:
               return customerFrom.get(Customer_.firstName);
           case LAST_NAME:
               return customerFrom.get(Customer_.lastName);
           case ADDRESS1:
               return customerFrom.get(Customer_.adress1);
           case ADDRESS2:
               return customerFrom.get(Customer_.adress2);
           case ZIP:
               return customerFrom.get(Customer_.zip);
           case CITY:
               return customerFrom.get(Customer_.city);
           case PHONE1:
               return customerFrom.get(Customer_.phone1);
           case PHONE2:
               return customerFrom.get(Customer_.phone2);
           case EMAIL:
               return customerFrom.get(Customer_.email);
           case NOTES:
               return customerFrom.get(Customer_.notes);
           default:
               throw new AssertionError(customerColumn);
       }
    }
}
