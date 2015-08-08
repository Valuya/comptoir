package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.auth.WsLoginResponse;
import be.valuya.comptoir.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.CompanyService;
import be.valuya.comptoir.service.EmployeeService;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsEmployeeConverter;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/login")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class LoginResource {

    @EJB
    private CompanyService companyService;
    @EJB
    private EmployeeService employeeService;
    @Inject
    private ToWsEmployeeConverter toWsEmployeeConverter;

    @POST
    public WsLoginResponse login(String login, String passwordHash) {
        Employee employee = employeeService.findEmployeeByLogin(login);
        if (employee == null) {
            throw new NotFoundException();
        }

        String token = employeeService.login(employee, passwordHash);

        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(employee);

        return new WsLoginResponse(employeeRef, token);
    }
}
