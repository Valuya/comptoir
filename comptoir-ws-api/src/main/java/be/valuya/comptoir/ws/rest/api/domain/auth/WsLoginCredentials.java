package be.valuya.comptoir.ws.rest.api.domain.auth;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cghislai
 */
@XmlRootElement(name = "LoginCredentials")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsLoginCredentials {

    @XmlElement
    private String login;
    @XmlElement
    private String pasword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }
}
