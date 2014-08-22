package be.valuya.comptoir.web.control;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.factory.CompanyFactory;
import be.valuya.comptoir.model.factory.EmployeeFactory;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.web.view.Views;
import java.io.Serializable;
import java.util.Locale;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class RegistrationController implements Serializable {

    @EJB
    private transient AccountService accountService;
    @Inject
    private transient LoginController loginController;
    @Inject
    private transient CompanyFactory companyFactory;
    @Inject
    private transient EmployeeFactory employeeFactory;

    private Company company;
    private Employee employee;

    public String actionShow() {
        company = companyFactory.createCompany();
        employee = employeeFactory.createEmployee(company);

        return Views.REGISTRATION_START;
    }

    public String actionRegister() {
        Locale userLocale = loginController.getUserLocale();
        employee.setLocale(userLocale);
        accountService.register(company, employee);
        return Views.REGISTRATION_DONE;
    }

    public Company getCompany() {
        return company;
    }

    public Employee getEmployee() {
        return employee;
    }

}
