package be.valuya.comptoir.ws.convert.thirdparty;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
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
    private FromWsCompanyConverter fromWsCompanyConverter;

    public Employee convert(WsEmployee wsEmployee) {
        if (wsEmployee == null) {
            return null;
        }
        Employee employee = new Employee();
        return patch(employee, wsEmployee);
    }

    public Employee patch(Employee employee, WsEmployee wsEmployee) {
        Long id = wsEmployee.getId();
        WsCompanyRef companyRef = wsEmployee.getCompanyRef();
        String firstName = wsEmployee.getFirstName();
        String lastName = wsEmployee.getLastName();
        Locale locale = wsEmployee.getLocale();
        String login = wsEmployee.getLogin();
        boolean active = wsEmployee.isActive();

        Company company = fromWsCompanyConverter.find(companyRef);

        employee.setId(id);
        employee.setCompany(company);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setLocale(locale);
        employee.setLogin(login);
        employee.setActive(active);

        return employee;
    }

}
