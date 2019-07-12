/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.auth.ComptoirPrincipal;
import be.valuya.comptoir.model.auth.EmployeePrincipal;
import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Field;
import java.security.Principal;

/**
 * @author cghislai
 */
@ApplicationScoped
public class EmployeeAccessChecker {

    @Inject
    private HttpServletRequest servletRequest;

    public void checkOwnCompany(WithCompany withCompany) {
        if (withCompany == null) {
            throw new ForbiddenException("Unauthorized");
        }
        Company company = withCompany.getCompany();
        this.checkOwnCompany(company);
    }

    public void checkOwnCompany(Company company) {
        if (company == null) {
            throw new ForbiddenException("Unauthorized");
        }
        Long companyId = company.getId();
        this.checkOwnCompany(companyId);
    }

    public void checkOwnCompany(long compayId) {
        Principal userPrincipal = this.glassfishWorkAround(servletRequest);
        if (!(userPrincipal instanceof ComptoirPrincipal)) {
            throw new NotAuthorizedException("Invalid credential");
        }
        EmployeePrincipal employeePrincipal = (EmployeePrincipal) userPrincipal;
        if (compayId != employeePrincipal.getCompanyId()) {
            throw new ForbiddenException("Unauthorized");
        }
    }

    public void checkSelf(Employee employee) {
        Principal userPrincipal = this.glassfishWorkAround(servletRequest);
        if (!(userPrincipal instanceof ComptoirPrincipal)) {
            throw new NotAuthorizedException("Invalid credential");
        }
        EmployeePrincipal employeePrincipal = (EmployeePrincipal) userPrincipal;

        if (employee == null || employee.getId() != employeePrincipal.getEmployeeId()) {
            throw new ForbiddenException("Unauthorized");
        }
    }

    private Principal glassfishWorkAround(HttpServletRequest request) {
        Principal principal = null;
        try {
            Principal webPrincipal = request.getUserPrincipal();
            if (webPrincipal != null) {
                Class glassfishWrapper = Class.forName("com.sun.enterprise.security.web.integration.WebPrincipal");
                if (glassfishWrapper.isInstance(webPrincipal)) {
                    Field customPrincipal = glassfishWrapper.getDeclaredField("customPrincipal");
                    customPrincipal.setAccessible(true);
                    principal = (Principal) customPrincipal.get(webPrincipal);
                } else {
                    principal = webPrincipal;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | ClassNotFoundException ex) {
            throw new WebApplicationException("Failed to extract principal in gf workaround");
        }
        return principal;
    }

}
