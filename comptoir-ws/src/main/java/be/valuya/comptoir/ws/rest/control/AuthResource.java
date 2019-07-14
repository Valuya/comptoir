package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.auth.WsAuth;
import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.security.ComptoirRoles;
import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.util.LoggedUser;
import be.valuya.comptoir.ws.api.AuthResourceApi;
import be.valuya.comptoir.ws.convert.auth.ToWsAuthConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

@RolesAllowed(ComptoirRoles.ACTIVE)
public class AuthResource implements AuthResourceApi {

    @EJB
    private AuthService authService;
    @Inject
    private ToWsAuthConverter toWsAuthConverter;
    @Inject
    @LoggedUser
    private Auth userAuth;
    @Inject
    private EmployeeAccessChecker accessChecker;


    public WsAuth login() {
        if (userAuth == null) {
            throw new NotAuthorizedException("No user logged in");
        }
        WsAuth wsAuth = toWsAuthConverter.convert(userAuth);
        return wsAuth;
    }


    public WsAuth refreshAuth(String refreshToken) {
        Auth auth = authService.refreshAuth(refreshToken);
        accessChecker.checkSelf(auth.getEmployee());
        WsAuth wsAuth = toWsAuthConverter.convert(auth);

        return wsAuth;
    }
}
