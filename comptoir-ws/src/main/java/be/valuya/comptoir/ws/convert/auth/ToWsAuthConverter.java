package be.valuya.comptoir.ws.convert.auth;

import be.valuya.comptoir.ws.rest.api.domain.auth.WsAuth;
import be.valuya.comptoir.ws.rest.api.domain.auth.WsAuthRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsEmployeeConverter;
import java.time.ZonedDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majorimport be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter; import
 * be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter; os <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsAuthConverter {

    @Inject
    private ToWsEmployeeConverter fromWsEmployeeConverter;

    public WsAuth convert(Auth auth) {
        if (auth == null) {
            return null;
        }
        Long id = auth.getId();
        ZonedDateTime expirationDateTime = auth.getExpirationDateTime();
        String refreshToken = auth.getRefreshToken();
        String token = auth.getToken();

        Employee employee = auth.getEmployee();
        WsEmployeeRef employeeRef = fromWsEmployeeConverter.reference(employee);

        WsAuth wsAuth = new WsAuth();
        wsAuth.setId(id);
        wsAuth.setEmployeeRef(employeeRef);
        wsAuth.setExpirationDateTime(expirationDateTime);
        wsAuth.setRefreshToken(refreshToken);
        wsAuth.setToken(token);

        return wsAuth;
    }

    public WsAuthRef reference(Auth auth) {
        Long id = auth.getId();
        WsAuthRef wsAuthRef = new WsAuthRef(id);
        return wsAuthRef;
    }

}
