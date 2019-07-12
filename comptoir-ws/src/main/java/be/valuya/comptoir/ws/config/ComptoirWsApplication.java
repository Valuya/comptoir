package be.valuya.comptoir.ws.config;

import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationPath("/")
@DeclareRoles({
        Roles.ANONYMOUS,
        Roles.ACTIVE,
        Roles.EMPLOYEE,
        Roles.MAINTENANCE,
})
public class ComptoirWsApplication {

    public ComptoirWsApplication() {
    }

}
