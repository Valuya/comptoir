/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.util.LoggedUser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

/**
 * @author cghislai
 */
@RequestScoped
public class EmployeeAccessChecker {

    @Inject
    @LoggedUser
    private Employee loggedEmployee;

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
        Company loggedEmployeeCompany = loggedEmployee.getCompany();
        Long expectedCompanyId = loggedEmployeeCompany.getId();
        if (compayId != expectedCompanyId) {
            throw new ForbiddenException("Unauthorized");
        }
    }

    public void checkSelf(Employee employee) {
        Long expectedEmployeeId = loggedEmployee.getId();
        if (employee == null || employee.getId() != expectedEmployeeId) {
            throw new ForbiddenException("Unauthorized");
        }
    }

}
