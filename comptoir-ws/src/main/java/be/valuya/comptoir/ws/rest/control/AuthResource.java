package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.auth.WsAuth;
import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.util.LoggedUser;
import be.valuya.comptoir.security.ComptoirRoles;
import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.ws.convert.auth.ToWsAuthConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import org.jboss.resteasy.spi.UnauthorizedException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({ComptoirRoles.EMPLOYEE})
@RequestScoped
public class AuthResource {

    @EJB
    private AuthService authService;
    @Inject
    private ToWsAuthConverter toWsAuthConverter;
    @Inject
    @LoggedUser
    private Auth userAuth;
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
        if (userAuth == null) {
            throw new UnauthorizedException();
        }
        WsAuth wsAuth = toWsAuthConverter.convert(userAuth);
        return wsAuth;
    }
}
