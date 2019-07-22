package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsCustomerConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsSaleSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsCustomerConverter fromWsCustomerConverter;

    public SaleSearch convert(WsSaleSearch wsSaleSearch) {
        if (wsSaleSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsSaleSearch.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);
        Boolean closed = wsSaleSearch.getClosed();
        ZonedDateTime fromDateTime = wsSaleSearch.getFromDateTime();
        ZonedDateTime toDateTime = wsSaleSearch.getToDateTime();
        WsCustomerRef customerRef = wsSaleSearch.getCustomerRef();
        Customer customer = fromWsCustomerConverter.find(customerRef);

        SaleSearch saleSearch = new SaleSearch();
        saleSearch.setCompany(company);
        saleSearch.setClosed(closed);
        saleSearch.setFromDateTime(fromDateTime);
        saleSearch.setToDateTime(toDateTime);
        saleSearch.setCustomer(customer);

        return saleSearch;
    }

}
