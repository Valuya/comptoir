package be.valuya.comptoir.api.domain.thirdparty;

import be.valuya.comptoir.api.domain.company.WsCompany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "registration")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsRegistration {

    @NotNull
    private WsCompany company;
    @NotNull
    private WsEmployee employee;
    @NotNull
    private String employeePassword;

    public WsCompany getCompany() {
        return company;
    }

    public void setCompany(WsCompany company) {
        this.company = company;
    }

    public WsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(WsEmployee employee) {
        this.employee = employee;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

}
