package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsCustomerSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.CustomerSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by cghislai on 23/03/16.
 */
@ApplicationScoped
public class FromWsCustomerSearchConverter {
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public CustomerSearch convert(WsCustomerSearch wsCustomerSearch) {
        String cityContains = wsCustomerSearch.getCityContains();
        WsCompanyRef companyRef = wsCustomerSearch.getCompanyRef();
        String emailContains = wsCustomerSearch.getEmailContains();
        String firstNameContains = wsCustomerSearch.getFirstNameContains();
        String lastNameContains = wsCustomerSearch.getLastNameContains();
        String multiSearch = wsCustomerSearch.getMultiSearch();
        String notesContains = wsCustomerSearch.getNotesContains();

        Company company = fromWsCompanyConverter.find(companyRef);

        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setCompany(company);
        customerSearch.setCityContains(cityContains);
        customerSearch.setEmailContains(emailContains);
        customerSearch.setFirstNameContains(firstNameContains);
        customerSearch.setLastNameContains(lastNameContains);
        customerSearch.setNotesContains(notesContains);
        customerSearch.setMultiSearch(multiSearch);
        return customerSearch;
    }
}
