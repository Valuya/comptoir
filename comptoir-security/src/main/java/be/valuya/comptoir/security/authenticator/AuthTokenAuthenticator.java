package be.valuya.comptoir.security.authenticator;

import be.valuya.comptoir.security.credential.AuthTokenCredential;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.callback.PasswordValidationCallback;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by cghislai on 12/02/17.
 */
public class AuthTokenAuthenticator implements Authenticator {
    public final static String TOKEN_LOGIN_PREFIX = "ComptoirAuthToken:";

    private final static Logger LOG = Logger.getLogger(AuthTokenAuthenticator.class.getCanonicalName());

    private PasswordValidationCallback validationCallback;

    @Override
    public boolean checkCredentials(Subject subject, HttpServletRequest request, HttpServletResponse response) {
        this.validationCallback = subject.getPrivateCredentials().stream()
                .filter(cred -> cred instanceof AuthTokenCredential)
                .map(cred -> ((AuthTokenCredential) cred))
                .findFirst()
                .map(cred -> this.createCallback(subject, cred))
                .orElse(null);
        return this.validationCallback != null;
    }


    @Override
    public void handleCallback(CallbackHandler callbackHandler) throws IOException, UnsupportedCallbackException {
        callbackHandler.handle(new Callback[]{validationCallback});
    }

    @Override
    public AuthStatus getAuthStatus() {
        if (this.validationCallback != null) {
            if (this.validationCallback.getResult()) {
                return AuthStatus.SUCCESS;
            }
        }
        return AuthStatus.FAILURE;
    }

    @Override
    public void clearAuthentication() {
        this.validationCallback = null;
    }

    // Currently delegates to the JAAS using vendor bridge.
    private PasswordValidationCallback createCallback(Subject subject, AuthTokenCredential cred) {
        String token = TOKEN_LOGIN_PREFIX + cred.getToken();
        PasswordValidationCallback validationCallback = new PasswordValidationCallback(subject, token, null);
        return validationCallback;
    }

}
