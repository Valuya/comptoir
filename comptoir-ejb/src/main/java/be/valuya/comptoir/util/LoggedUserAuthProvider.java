package be.valuya.comptoir.util;

import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.security.ComptoirPrincipal;
import be.valuya.comptoir.security.UnauthenticatedException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;

@ApplicationScoped
public class LoggedUserAuthProvider {

    @Inject
    private SecurityContext securityContext;

    @Produces
    @RequestScoped
    @LoggedUser
    public Auth getLoggedUserAuth()  {
        Principal callerPrincipal = securityContext.getCallerPrincipal();
        if (callerPrincipal == null) {
            throw new UnauthenticatedException();
        }
        if (!ComptoirPrincipal.class.isAssignableFrom(callerPrincipal.getClass())) {
            throw new UnauthenticatedException();
        }
        ComptoirPrincipal comptoirPrincipal = (ComptoirPrincipal) callerPrincipal;
        Auth auth = comptoirPrincipal.getAuth();
        return auth;
    }

    @Produces
    @RequestScoped
    @LoggedUser
    public Employee getLoggedEmployee(@LoggedUser Auth auth) {
        return auth.getEmployee();
    }

    @Produces
    @RequestScoped
    @LoggedUser
    public Company getLoggedUserCompany(@LoggedUser Auth auth) {
        return auth.getEmployee().getCompany();
    }
}
