package be.valuya.comptoir.model.thirdparty;

import be.valuya.comptoir.service.EmployeeService;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class UniqueEmployeeLoginValidator implements ConstraintValidator<UniqueEmployeeLoginValidation, Employee> {

    @Inject
    private EmployeeService employeeService;

    @Override
    public void initialize(UniqueEmployeeLoginValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Employee employee, ConstraintValidatorContext context) {
        if (employee == null) {
            return true;
        }
        if (employeeService == null) {
            return true;
        }
        String login = employee.getLogin();
        Employee foundEmployee = employeeService.findEmployeeByLogin(login);
        if (foundEmployee == null) {
            return true;
        }
        return foundEmployee.equals(employee);
    }

}
