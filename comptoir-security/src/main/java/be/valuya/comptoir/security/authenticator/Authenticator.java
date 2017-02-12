package be.valuya.comptoir.security.authenticator;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by cghislai on 12/02/17.
 */
public interface Authenticator {

    /**
     * Phase 1: check if subject credentials contains one matching
     *
     * @param subject
     * @param request
     * @param response
     * @return
     */
    boolean checkCredentials(Subject subject, HttpServletRequest request, HttpServletResponse response);

    /**
     * Phase 2: make the callback handler handle this authenticator callabcks
     *
     * @param callbackHandler
     * @throws IOException
     * @throws UnsupportedCallbackException
     */
    void handleCallback(CallbackHandler callbackHandler) throws IOException, UnsupportedCallbackException;

    /**
     * Phase 3: return status after callback have been handled
     *
     * @return
     */
    AuthStatus getAuthStatus();

    /**
     * Phase 4: cleanup
     */
    void clearAuthentication();
}
