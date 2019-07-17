package be.valuya.comptoir.ws.convert.thirdparty;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsEmployeeConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsEmployee convert(Employee employee) {
        if (employee == null) {
            return null;
        }
        Long id = employee.getId();
        Company company = employee.getCompany();
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        Locale locale = employee.getLocale();
        String login = employee.getLogin();
        boolean active = employee.isActive();

        WsCompanyRef wsCompanyRef = toWsCompanyConverter.reference(company);

        WsEmployee wsEmployee = new WsEmployee();
        wsEmployee.setId(id);
        wsEmployee.setCompanyRef(wsCompanyRef);
        wsEmployee.setFirstName(firstName);
        wsEmployee.setLastName(lastName);
        wsEmployee.setLocale(locale);
        wsEmployee.setLogin(login);
        wsEmployee.setActive(active);

        return wsEmployee;
    }

    public WsEmployeeRef reference(Employee employee) {
        Long id = employee.getId();
        WsEmployeeRef wsEmployeeRef = new WsEmployeeRef(id);
        return wsEmployeeRef;
    }

}
