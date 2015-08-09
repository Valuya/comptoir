package be.valuya.comptoir.api.domain.auth;

import be.valuya.comptoir.api.domain.thirdparty.WsEmployeeRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "LoginResponse")
public class WsLoginResponse {

    private WsEmployeeRef employeeRef;
    private String authToken;

    public WsLoginResponse() {
    }

    public WsLoginResponse(WsEmployeeRef employeeRef, String authToken) {
        this.employeeRef = employeeRef;
        this.authToken = authToken;
    }

    public WsEmployeeRef getEmployeeRef() {
        return employeeRef;
    }

    public void setEmployeeRef(WsEmployeeRef employeeRef) {
        this.employeeRef = employeeRef;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
