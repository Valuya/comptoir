package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoice;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoiceSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsInvoiceSearch;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.search.InvoiceSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.InvoiceService;
import be.valuya.comptoir.ws.convert.commercial.FromWsInvoiceConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsInvoiceConverter;
import be.valuya.comptoir.ws.convert.search.FromWsInvoiceSearchConverter;
import be.valuya.comptoir.ws.rest.api.InvoiceResourceApi;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class InvoiceResource implements InvoiceResourceApi {

    @EJB
    private InvoiceService invoiceService;
    @Inject
    private FromWsInvoiceConverter fromWsInvoiceConverter;
    @Inject
    private FromWsInvoiceSearchConverter fromWsInvoiceSearchConverter;
    @Inject
    private ToWsInvoiceConverter toWsInvoiceConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private EmployeeAccessChecker accessChecker;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private UriInfo uriInfo;

    public WsInvoiceRef createInvoice(WsInvoice wsInvoice) {
        Invoice invoice = fromWsInvoiceConverter.convert(wsInvoice);
        accessChecker.checkOwnCompany(invoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        WsInvoiceRef invoiceRef = toWsInvoiceConverter.reference(savedInvoice);
        return invoiceRef;
    }

    public WsInvoiceRef updateInvoice(long id, WsInvoice wsInvoice) {
        idChecker.checkId(id, wsInvoice);
        Invoice invoice = fromWsInvoiceConverter.convert(wsInvoice);
        accessChecker.checkOwnCompany(invoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        WsInvoiceRef invoiceRef = toWsInvoiceConverter.reference(savedInvoice);
        return invoiceRef;
    }

    public WsInvoice getInvoice(long id) {
        Invoice invoice = invoiceService.findInvoiceById(id);
        accessChecker.checkOwnCompany(invoice);

        WsInvoice wsInvoice = toWsInvoiceConverter.convert(invoice);

        return wsInvoice;
    }

    public WsInvoiceSearchResult findInvoices(WsInvoiceSearch wsInvoiceSearch) {
        Pagination<Object, ?> pagination = restPaginationUtil.extractPagination(uriInfo);
        InvoiceSearch invoiceSearch = fromWsInvoiceSearchConverter.convert(wsInvoiceSearch);
        accessChecker.checkOwnCompany(invoiceSearch);
        List<Invoice> invoices = invoiceService.findInvoices(invoiceSearch);

        List<WsInvoiceRef> wsInvoices = invoices.stream()
                .map(toWsInvoiceConverter::reference)
                .collect(Collectors.toList());

        return restPaginationUtil.setResults(new WsInvoiceSearchResult(), wsInvoices, pagination);
    }

}
