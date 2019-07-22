package be.valuya.comptoir.model.auth;

/**
 * Created by cghislai on 12/02/17.
 */
public class EmployeePrincipal extends ComptoirPrincipal {
    long employeeId;
    long companyId;
    boolean active;
    String login;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
