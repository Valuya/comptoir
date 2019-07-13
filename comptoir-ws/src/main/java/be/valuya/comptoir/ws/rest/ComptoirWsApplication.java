package be.valuya.comptoir.ws.rest;

import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.DeclareRoles;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationPath("/")
@DeclareRoles({
        ComptoirRoles.ANONYMOUS,
        ComptoirRoles.ACTIVE,
        ComptoirRoles.EMPLOYEE,
        ComptoirRoles.MAINTENANCE,
})
@ServletSecurity(@HttpConstraint(
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL
))
public class ComptoirWsApplication extends Application {

    public ComptoirWsApplication() {
    }

}
