package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.ejb.Singleton;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Singleton
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
