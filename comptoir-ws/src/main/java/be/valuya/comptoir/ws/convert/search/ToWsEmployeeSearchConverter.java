package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.search.WsEmployeeSearch;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.EmployeeSearch;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsEmployeeSearchConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsEmployeeSearch convert(EmployeeSearch employeeSearch) {
        Company company = employeeSearch.getCompany();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsEmployeeSearch wsEmployeeSearch = new WsEmployeeSearch();
        wsEmployeeSearch.setCompanyRef(companyRef);

        return wsEmployeeSearch;
    }

}
