package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.api.domain.thirdparty.WsRegistration;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.RegistrationService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsEmployeeConverter;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/registration")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RegistrationResource {

    @EJB
    private RegistrationService registrationService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsEmployeeConverter fromWsEmployeeConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    @POST
    @Valid
    public WsCompanyRef register(@Valid WsRegistration registration) {
        WsCompany wsCompany = registration.getCompany();
        WsEmployee wsEmployee = registration.getEmployee();
        String employeePassword = registration.getEmployeePassword();

        Company company = fromWsCompanyConverter.convert(wsCompany);

        Employee employee = fromWsEmployeeConverter.convert(wsEmployee);

        Company createdCompany = registrationService.register(company, employee, employeePassword);

        return toWsCompanyConverter.reference(createdCompany);
    }
}
