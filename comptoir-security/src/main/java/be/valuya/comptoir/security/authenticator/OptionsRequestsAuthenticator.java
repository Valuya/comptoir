package be.valuya.comptoir.security.authenticator;

import be.valuya.comptoir.security.ComptoirAuthModuleOptions;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by cghislai on 12/02/17.
 */
public class OptionsRequestsAuthenticator implements Authenticator {

    private final static Logger LOG = Logger.getLogger(OptionsRequestsAuthenticator.class.getSimpleName());
    private boolean optionsRequest;
    private final ComptoirAuthModuleOptions authModuleOptions;

    public OptionsRequestsAuthenticator(ComptoirAuthModuleOptions authModuleOptions) {
        this.authModuleOptions = authModuleOptions;
    }

    @Override
    public boolean checkCredentials(Subject subject, HttpServletRequest request, HttpServletResponse response) {
        this.optionsRequest = request.getMethod() == "OPTIONS";
        if (optionsRequest) {
            String allowedCorsOrigin = authModuleOptions.getAllowedCorsOrigin();
            boolean allowAnyCorsOrigin = authModuleOptions.isAllowAnyCorsOrigin();
            String corsOrigin = this.getCorsOrigin(request, allowedCorsOrigin, allowAnyCorsOrigin);
            response.addHeader("Access-Control-Allow-Origin", corsOrigin);
            response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "content-type, accept, accept-charset, authorization, X-Requested-With");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Expose-Headers", "accept-ranges, content-encoding, content-length, X-Comptoir-ListTotalCount");
        }
        return optionsRequest;
    }

    @Override
    public void handleCallback(CallbackHandler callbackHandler) {
    }

    @Override
    public AuthStatus getAuthStatus() {
        if (this.optionsRequest) {
            return AuthStatus.SEND_SUCCESS;
        } else {
            return AuthStatus.FAILURE;
        }
    }

    @Override
    public void clearAuthentication() {
        this.optionsRequest = false;
    }

    private String getCorsOrigin(HttpServletRequest request, String origin, boolean allowAny) {
        if (allowAny) {
            String requestOrigin = request.getHeader("Origin");
            return requestOrigin;
        }
        return origin;
    }

}
