package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompany;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsRegistration;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.RegistrationService;
import be.valuya.comptoir.ws.rest.api.RegistrationResourceApi;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsEmployeeConverter;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@PermitAll
public class RegistrationResource implements RegistrationResourceApi {

    @EJB
    private RegistrationService registrationService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsEmployeeConverter fromWsEmployeeConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsCompanyRef register(WsRegistration registration) {
        WsCompany wsCompany = registration.getCompany();
        WsEmployee wsEmployee = registration.getEmployee();
        String employeePassword = registration.getEmployeePassword();

        Company company = fromWsCompanyConverter.convert(wsCompany);

        Employee employee = fromWsEmployeeConverter.convert(wsEmployee);

        Company createdCompany = registrationService.register(company, employee, employeePassword);

        return toWsCompanyConverter.reference(createdCompany);
    }
}
