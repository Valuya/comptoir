package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.auth.WsAuth;
import be.valuya.comptoir.api.domain.auth.WsLoginCredentials;
import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.service.EmployeeService;
import be.valuya.comptoir.ws.convert.auth.ToWsAuthConverter;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AuthResource {

    @EJB
    private EmployeeService employeeService;
    @EJB
    private AuthService authService;
    @Inject
    private ToWsAuthConverter toWsAuthConverter;

    @GET
    @Path("/refresh/{refreshToken}")
    public WsAuth refreshAuth(@PathParam("refreshToken") String refreshToken) {
        Auth auth = authService.refreshAuth(refreshToken);

        WsAuth wsAuth = toWsAuthConverter.convert(auth);

        return wsAuth;
    }

    @POST
    public WsAuth login(WsLoginCredentials credentials) {
        String login = credentials.getLogin();
        Employee employee = employeeService.findEmployeeByLogin(login);
        if (employee == null) {
            throw new NotFoundException();
        }

        String passwordHash = credentials.getPasswordHash();

        Auth auth = authService.login(employee, passwordHash);

        WsAuth wsAuth = toWsAuthConverter.convert(auth);

        return wsAuth;
    }
}
