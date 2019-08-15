package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountingTransactionConverter;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsCustomerConverter;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsSaleConverter {

    @EJB
    private SaleService saleService;
    @Inject
    private FromWsAccountingTransactionConverter fromWsAccountingTransactionConverter;
    @Inject
    private FromWsCustomerConverter fromWsCustomerConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsInvoiceConverter fromWsInvoiceConverter;

    public Sale convert(WsSale wsSale) {
        if (wsSale == null) {
            return null;
        }
        Sale sale = new Sale();

        return patch(sale, wsSale);
    }

    public Sale patch(Sale sale, WsSale wsSale) {
        Long id = wsSale.getId();
        ZonedDateTime dateTime = wsSale.getDateTime();
        String reference = wsSale.getReference();
        boolean closed = wsSale.isClosed();

        WsAccountingTransactionRef accountingTransactionRef = wsSale.getAccountingTransactionRef();
        AccountingTransaction accountingTransaction = fromWsAccountingTransactionConverter.find(accountingTransactionRef);

        WsCustomerRef customerRef = wsSale.getCustomerRef();
        Customer customer = fromWsCustomerConverter.find(customerRef);

        WsInvoiceRef invoiceRef = wsSale.getInvoiceRef();
        Invoice invoice = fromWsInvoiceConverter.find(invoiceRef);

        WsCompanyRef companyRef = wsSale.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        sale.setId(id);
        sale.setCompany(company);
        sale.setReference(reference);
        sale.setClosed(closed);
        sale.setCustomer(customer);
        sale.setDateTime(dateTime);
        sale.setInvoice(invoice);

        if (accountingTransaction != null) {
            sale.setAccountingTransaction(accountingTransaction);
        }
        return sale;
    }

    public Sale find(WsSaleRef saleRef) {
        if (saleRef == null) {
            return null;
        }
        Long saleId = saleRef.getId();
        Sale sale = saleService.findSaleById(saleId);
        return sale;
    }

}
