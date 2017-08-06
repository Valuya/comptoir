package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.auth.WsAuth;
import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.service.EmployeeService;
import be.valuya.comptoir.ws.convert.auth.ToWsAuthConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({Roles.EMPLOYEE})
public class AuthResource {

    @EJB
    private EmployeeService employeeService;
    @EJB
    private AuthService authService;
    @Inject
    private ToWsAuthConverter toWsAuthConverter;
    @Inject
    private Principal principal;
    @Context
    private SecurityContext securityContext;
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    @Path("/refresh/{refreshToken}")
    @Valid
    public WsAuth refreshAuth(@PathParam("refreshToken") String refreshToken) {
        Auth auth = authService.refreshAuth(refreshToken);
        accessChecker.checkSelf(auth.getEmployee());
        WsAuth wsAuth = toWsAuthConverter.convert(auth);

        return wsAuth;
    }

    @POST
    @Valid
    public WsAuth login() {
        String loggedEmployeePrincipalName = this.principal.getName();
        Employee employee = employeeService.findEmployeeByLogin(loggedEmployeePrincipalName);
        if (employee == null) {
            throw new NotFoundException();
        }

        Auth auth = authService.login(employee, employee.getPasswordHash());
        WsAuth wsAuth = toWsAuthConverter.convert(auth);

        return wsAuth;
    }
}
