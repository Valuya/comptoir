package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.auth.WsAuth;
import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.util.LoggedUser;
import be.valuya.comptoir.ws.rest.api.AuthResourceApi;
import be.valuya.comptoir.ws.convert.auth.ToWsAuthConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

@RolesAllowed(ComptoirRoles.ACTIVE)
public class AuthResource implements AuthResourceApi {

    @EJB
    private AuthService authService;
    @Inject
    private ToWsAuthConverter toWsAuthConverter;
    @Inject
    private EmployeeAccessChecker accessChecker;


    public WsAuth login(String header) {
        return authService.getAuthOptional()
                .map(toWsAuthConverter::convert)
                .orElseThrow(() -> new NotAuthorizedException("no user logged in"));

    }


    public WsAuth refreshAuth(String refreshToken) {
        Auth auth = authService.refreshAuth(refreshToken);
        accessChecker.checkSelf(auth.getEmployee());
        WsAuth wsAuth = toWsAuthConverter.convert(auth);

        return wsAuth;
    }
}
