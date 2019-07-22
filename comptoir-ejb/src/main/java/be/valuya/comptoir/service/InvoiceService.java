package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.commercial.Invoice_;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.InvoiceSearch;

import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class InvoiceService {

    @PersistenceContext
    private EntityManager entityManager;

    @Nonnull
    public List<Invoice> findInvoices(@Nonnull InvoiceSearch invoiceSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = criteriaBuilder.createQuery(Invoice.class);
        Root<Invoice> invoiceRoot = query.from(Invoice.class);

        List<Predicate> predicates = new ArrayList<>();

        Company company = invoiceSearch.getCompany();
        Path<Company> companyPath = invoiceRoot.get(Invoice_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);
        predicates.add(companyPredicate);

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        query.where(predicateArray);

        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query);

        List<Invoice> invoices = typedQuery.getResultList();

        return invoices;
    }

    public Invoice findInvoiceById(Long id) {
        return entityManager.find(Invoice.class, id);
    }

    public Invoice saveInvoice(Invoice invoice) {
        return entityManager.merge(invoice);
    }

}
