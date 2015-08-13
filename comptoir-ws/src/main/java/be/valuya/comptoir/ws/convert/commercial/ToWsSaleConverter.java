package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.api.domain.commercial.WsSale;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingTransactionConverter;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsCustomerConverter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsSaleConverter {

    @Inject
    private ToWsAccountingTransactionConverter toWsAccountingTransactionConverter;
    @Inject
    private ToWsCustomerConverter toWsCustomerConverter;
    @Inject
    private ToWsInvoiceConverter toWsInvoiceConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsSale convert(Sale sale) {
        if (sale == null) {
            return null;
        }
        Long id = sale.getId();
        ZonedDateTime dateTime = sale.getDateTime();
        String reference = sale.getReference();
        BigDecimal vatAmount = sale.getVatAmount();
        BigDecimal vatExclusiveAmout = sale.getVatExclusiveAmout();
        boolean closed = sale.isClosed();

        AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        WsAccountingTransactionRef accountingTransactionRef = toWsAccountingTransactionConverter.reference(accountingTransaction);

        Customer customer = sale.getCustomer();
        WsCustomerRef customerRef = toWsCustomerConverter.reference(customer);

        Invoice invoice = sale.getInvoice();
        WsInvoiceRef invoiceRef = toWsInvoiceConverter.reference(invoice);

        Company company = sale.getCompany();
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsSale wsSale = new WsSale();
        wsSale.setId(id);
        wsSale.setCompanyRef(companyRef);
        wsSale.setReference(reference);
        wsSale.setAccountingTransactionRef(accountingTransactionRef);
        wsSale.setClosed(closed);
        wsSale.setCustomerRef(customerRef);
        wsSale.setDateTime(dateTime);
        wsSale.setInvoiceRef(invoiceRef);
        wsSale.setVatAmount(vatAmount);
        wsSale.setVatExclusiveAmout(vatExclusiveAmout);

        return wsSale;
    }

    public WsSaleRef reference(Sale sale) {
        if (sale == null) {
            return null;
        }
        Long id = sale.getId();
        return new WsSaleRef(id);
    }

}
