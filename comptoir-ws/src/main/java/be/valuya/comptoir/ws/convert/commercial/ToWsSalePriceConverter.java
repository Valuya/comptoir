package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.api.domain.commercial.WsSale;
import be.valuya.comptoir.api.domain.commercial.WsSalePrice;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.commercial.Invoice;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
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
public class ToWsSalePriceConverter {

    public WsSalePrice convert(SalePrice salePrice) {
        if (salePrice == null) {
            return null;
        }
        BigDecimal base = salePrice.getBase();
        BigDecimal taxes = salePrice.getTaxes();

        WsSalePrice wsSalePrice = new WsSalePrice();
        wsSalePrice.setBase(base);
        wsSalePrice.setTaxes(taxes);
        return wsSalePrice;
    }

}
