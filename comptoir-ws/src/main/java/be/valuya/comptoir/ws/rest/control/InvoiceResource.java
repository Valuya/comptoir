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
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/invoice")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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

    @POST
    public WsInvoiceRef createInvoice(@NoId WsInvoice wsInvoice) {
        return saveInvoice(wsInvoice);
    }

    @Valid
    @Path("{id}")
    @PUT
    public WsInvoiceRef saveInvoice(@PathParam("id") long id, @Valid WsInvoice wsInvoice) {
        idChecker.checkId(id, wsInvoice);
        return saveInvoice(wsInvoice);
    }

    @Valid
    @Path("{id}")
    @GET
    public WsInvoice getInvoice(@PathParam("id") long id) {
        Invoice invoice = invoiceService.findInvoiceById(id);

        WsInvoice wsInvoice = toWsInvoiceConverter.convert(invoice);

        return wsInvoice;
    }

    @Path("search")
    @Valid
    @POST
    public List<WsInvoice> findInvoices(@Valid WsInvoiceSearch wsInvoiceSearch) {
        InvoiceSearch invoiceSearch = fromWsInvoiceSearchConverter.convert(wsInvoiceSearch);
        List<Invoice> invoices = invoiceService.findInvoices(invoiceSearch);

        List<WsInvoice> wsInvoices = invoices.stream()
                .map(toWsInvoiceConverter::convert)
                .collect(Collectors.toList());

        return wsInvoices;
    }

    private WsInvoiceRef saveInvoice(WsInvoice wsInvoice) {
        Invoice invoice = fromWsInvoiceConverter.convert(wsInvoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

        WsInvoiceRef invoiceRef = toWsInvoiceConverter.reference(savedInvoice);

        return invoiceRef;
    }

}
