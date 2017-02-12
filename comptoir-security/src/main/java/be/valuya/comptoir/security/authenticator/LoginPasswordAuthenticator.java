package be.valuya.comptoir.security.authenticator;

import javax.resource.spi.security.PasswordCredential;
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
public class LoginPasswordAuthenticator implements Authenticator {

    private final static Logger LOG = Logger.getLogger(LoginPasswordAuthenticator.class.getCanonicalName());

    private PasswordValidationCallback passwordValidationCallback;

    @Override
    public boolean checkCredentials(Subject subject, HttpServletRequest request, HttpServletResponse response) {
        this.passwordValidationCallback = subject.getPrivateCredentials().stream()
                .filter(credential -> credential instanceof PasswordCredential)
                .findFirst()
                .map(cred -> {
                    PasswordCredential passwordCredential = (PasswordCredential) cred;
                    return new PasswordValidationCallback(subject, passwordCredential.getUserName(), passwordCredential.getPassword());
                })
                .orElse(null);
        return this.passwordValidationCallback != null;
    }

    @Override
    public void handleCallback(CallbackHandler callbackHandler) throws IOException, UnsupportedCallbackException {
        callbackHandler.handle(new Callback[]{passwordValidationCallback});
    }

    @Override
    public AuthStatus getAuthStatus() {
        if (this.passwordValidationCallback != null) {
            if (this.passwordValidationCallback.getResult()) {
                return AuthStatus.SUCCESS;
            }
        }
        return AuthStatus.FAILURE;
    }

    @Override
    public void clearAuthentication() {
        this.passwordValidationCallback = null;
    }


}
