package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsEmployeeSearch;
import be.valuya.comptoir.api.domain.search.WsLocaleSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.EmployeeSearch;
import be.valuya.comptoir.model.search.LocaleSearch;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsEmployeeSearchConverter {

    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsLocaleSearchConverter fromWsLocaleSearchConverter;

    public EmployeeSearch convert(WsEmployeeSearch wsEmployeeSearch) {
        if (wsEmployeeSearch == null) {
            return null;
        }
        WsCompanyRef companyRef = wsEmployeeSearch.getCompanyRef();

        Company company = fromWsCompanyConverter.find(companyRef);

        WsLocaleSearch wsLocaleSearch = wsEmployeeSearch.getLocaleSearch();
        LocaleSearch localeSearch = fromWsLocaleSearchConverter.convert(wsLocaleSearch);

        EmployeeSearch employeeSearch = new EmployeeSearch();
        employeeSearch.setCompany(company);
        employeeSearch.setLocaleSearch(localeSearch);

        return employeeSearch;
    }

}
