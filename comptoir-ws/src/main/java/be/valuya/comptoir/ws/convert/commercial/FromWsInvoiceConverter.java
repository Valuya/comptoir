package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsInvoice;
import be.valuya.comptoir.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.InvoiceService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsInvoiceConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @EJB
    private InvoiceService invoiceService;

    public Invoice convert(WsInvoice wsInvoice) {
        Long id = wsInvoice.getId();
        String number = wsInvoice.getNumber();
        String note = wsInvoice.getNote();

        WsCompanyRef companyRef = wsInvoice.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsSaleRef saleRef = wsInvoice.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setNote(note);
        invoice.setNumber(number);
        invoice.setSale(sale);
        invoice.setCompany(company);

        return invoice;
    }

    public Invoice find(WsInvoiceRef invoiceRef) {
        Long id = invoiceRef.getId();
        return invoiceService.findInvoiceById(id);
    }

}
