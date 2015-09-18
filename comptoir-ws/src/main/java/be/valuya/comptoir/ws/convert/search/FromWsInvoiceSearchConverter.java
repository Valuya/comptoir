package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsInvoiceSearch;
import be.valuya.comptoir.api.domain.search.WsLocaleSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.InvoiceSearch;
import be.valuya.comptoir.model.search.LocaleSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsInvoiceSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsLocaleSearchConverter fromWsLocaleSearchConverter;

    public InvoiceSearch convert(WsInvoiceSearch wsInvoiceSearch) {
        if (wsInvoiceSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsInvoiceSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsLocaleSearch wsLocaleSearch = wsInvoiceSearch.getLocaleSearch();
        LocaleSearch localeSearch = fromWsLocaleSearchConverter.convert(wsLocaleSearch);

        InvoiceSearch invoiceSearch = new InvoiceSearch();
        invoiceSearch.setCompany(company);
        invoiceSearch.setLocaleSearch(localeSearch);
        
        return invoiceSearch;
    }

}
