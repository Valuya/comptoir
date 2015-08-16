/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.Sale_;
import be.valuya.comptoir.util.pagination.SaleColumn;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author cghislai
 */
public class SaleColumnPersistenceUtils {
      public static List<Order> createOrdersFromSortings(CriteriaBuilder criteriaBuilder, Root<Sale> saleRoot, List<Sort<SaleColumn>> sortings) {
        if (sortings == null) {
            return new ArrayList<>();
        }
        List<Order> orders = sortings.stream().map(
                sorting -> createOrderFromSorting(criteriaBuilder, saleRoot, sorting))
                .collect(Collectors.toList());
        return orders;
    }

    public static Order createOrderFromSorting(CriteriaBuilder criteriaBuilder, Root<Sale> saleRoot, Sort<SaleColumn> sorting) {
        SaleColumn sortColumn = sorting.getSortColumn();
        Path<?> path = getPath(saleRoot, sortColumn);

        Order order;
        if (sorting.isAscending()) {
            order = criteriaBuilder.asc(path);
        } else {
            order = criteriaBuilder.desc(path);
        }
        return order;
    }

    public static Path<?> getPath(Root<Sale> saleRoot, SaleColumn column) {
        switch (column) {
            case DATETIME:
                return saleRoot.get(Sale_.dateTime);
            case VAT_AMOUNT:
                return saleRoot.get(Sale_.vatAmount);
            case VAT_EXCLUSIVE: 
                return saleRoot.get(Sale_.vatExclusiveAmount);
            default:
                throw new AssertionError(column.name());
        }
    }
}
