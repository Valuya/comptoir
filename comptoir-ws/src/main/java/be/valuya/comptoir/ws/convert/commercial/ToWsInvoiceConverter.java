package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsInvoice;
import be.valuya.comptoir.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsInvoiceConverter {

    @Inject
    private ToWsSaleConverter toWsSaleConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsInvoice convert(Invoice invoice) {
        Long id = invoice.getId();
        String note = invoice.getNote();
        String number = invoice.getNumber();

        Company company = invoice.getCompany();
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);
        
        Sale sale = invoice.getSale();
        WsSaleRef  saleRef = toWsSaleConverter.reference(sale);

        WsInvoice wsInvoice = new WsInvoice();
        wsInvoice.setId(id);
        wsInvoice.setCompanyRef(companyRef);

        return wsInvoice;
    }

    public WsInvoiceRef reference(Invoice invoice) {
        Long id = invoice.getId();
        WsInvoiceRef wsInvoiceRef = new WsInvoiceRef(id);
        return wsInvoiceRef;
    }

}
