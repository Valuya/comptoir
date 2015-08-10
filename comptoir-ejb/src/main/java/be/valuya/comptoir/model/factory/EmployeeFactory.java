package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.ejb.Stateless;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class EmployeeFactory {

    public Employee createEmployee(@Nonnull Company company) {
        Locale locale = Locale.ENGLISH;

        Employee employee = new Employee();
        employee.setCompany(company);
        employee.setLocale(locale);
        employee.setActive(true);

        return employee;
    }

}
