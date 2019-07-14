package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsInvoice;
import be.valuya.comptoir.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.api.domain.search.WsInvoiceSearch;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.search.InvoiceSearch;
import be.valuya.comptoir.service.InvoiceService;
import be.valuya.comptoir.ws.convert.commercial.FromWsInvoiceConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsInvoiceConverter;
import be.valuya.comptoir.ws.convert.search.FromWsInvoiceSearchConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.api.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/invoice")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class InvoiceResource {

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

    @POST
    public WsInvoiceRef createInvoice(@NoId WsInvoice wsInvoice) {
        Invoice invoice = fromWsInvoiceConverter.convert(wsInvoice);
        accessChecker.checkOwnCompany(invoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        WsInvoiceRef invoiceRef = toWsInvoiceConverter.reference(savedInvoice);
        return invoiceRef;
    }

    @Valid
    @Path("{id}")
    @PUT
    public WsInvoiceRef saveInvoice(@PathParam("id") long id, @Valid WsInvoice wsInvoice) {
        idChecker.checkId(id, wsInvoice);
        Invoice invoice = fromWsInvoiceConverter.convert(wsInvoice);
        accessChecker.checkOwnCompany(invoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        WsInvoiceRef invoiceRef = toWsInvoiceConverter.reference(savedInvoice);
        return invoiceRef;
    }

    @Valid
    @Path("{id}")
    @GET
    public WsInvoice getInvoice(@PathParam("id") long id) {
        Invoice invoice = invoiceService.findInvoiceById(id);
        accessChecker.checkOwnCompany(invoice);

        WsInvoice wsInvoice = toWsInvoiceConverter.convert(invoice);

        return wsInvoice;
    }

    @Path("search")
    @Valid
    @POST
    public List<WsInvoice> findInvoices(@Valid WsInvoiceSearch wsInvoiceSearch) {
        InvoiceSearch invoiceSearch = fromWsInvoiceSearchConverter.convert(wsInvoiceSearch);
        accessChecker.checkOwnCompany(invoiceSearch);
        List<Invoice> invoices = invoiceService.findInvoices(invoiceSearch);

        List<WsInvoice> wsInvoices = invoices.stream()
                .map(toWsInvoiceConverter::convert)
                .collect(Collectors.toList());

        return wsInvoices;
    }

}
