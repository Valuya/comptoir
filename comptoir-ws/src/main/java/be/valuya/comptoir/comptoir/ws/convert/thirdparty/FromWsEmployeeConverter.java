package be.valuya.comptoir.comptoir.ws.convert.thirdparty;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsEmployeeConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public Employee convert(WsEmployee wsEmployee) {
        Long id = wsEmployee.getId();
        WsCompanyRef companyRef = wsEmployee.getCompanyRef();
        String firstName = wsEmployee.getFirstName();
        String lastName = wsEmployee.getLastName();
        Locale locale = wsEmployee.getLocale();
        String login = wsEmployee.getLogin();

        Company company = fromWsCompanyConverter.find(companyRef);

        Employee employee = new Employee();
        employee.setId(id);
        employee.setCompany(company);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setLocale(locale);
        employee.setLogin(login);

        return employee;
    }

}
