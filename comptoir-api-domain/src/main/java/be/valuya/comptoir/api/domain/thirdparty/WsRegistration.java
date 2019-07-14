package be.valuya.comptoir.api.domain.thirdparty;

import be.valuya.comptoir.api.domain.company.WsCompany;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "Registration")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A company registration")
public class WsRegistration {

    @NotNull
    @Schema(required = true, description = "The company")
    private WsCompany company;
    @NotNull
    @Schema(required = true, description = "The administrator empoyee account")
    private WsEmployee employee;
    @NotNull
    @Schema(required = true, description = "The administrator empoyee password")
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
