package be.valuya.comptoir.auth.domain;

/**
 * Created by cghislai on 12/02/17.
 */
public class EmployeePrincipal extends ComptoirPrincipal {
    long employeeId;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
}
