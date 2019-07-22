package be.valuya.comptoir.ws.rest.api.domain.auth;

import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "LoginResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "Auth token")
public class WsAuth {

    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private WsEmployeeRef employeeRef;
    @Schema(required = true)
    private String token;
    @Schema(required = true)
    private String refreshToken;
    @Schema(required = true)
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime expirationDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsEmployeeRef getEmployeeRef() {
        return employeeRef;
    }

    public void setEmployeeRef(WsEmployeeRef employeeRef) {
        this.employeeRef = employeeRef;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public ZonedDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(ZonedDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsAuth other = (WsAuth) obj;
        return Objects.equals(this.id, other.id);
    }

}
