package be.valuya.comptoir.auth.glassfish;

import be.valuya.comptoir.auth.domain.ComptoirPrincipal;
import be.valuya.comptoir.security.authenticator.AuthTokenAuthenticator;
import com.sun.enterprise.security.BasePasswordLoginModule;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by cghislai on 12/02/17.
 */
public class ComptoirPasswordLoginModule extends BasePasswordLoginModule {

    private final static Logger LOG = Logger.getLogger(ComptoirPasswordLoginModule.class.getCanonicalName());


    @Override
    protected void authenticateUser() throws LoginException {
        if (!(_currentRealm instanceof ComptoirRealm)) {
            throw new LoginException("Bad realm");
        }
        if (this._username == null) {
            throw new LoginException("No login");
        }

        final ComptoirRealm comptoirRealm = (ComptoirRealm) _currentRealm;
        ComptoirPrincipal comptoirPrincipal;
        if (this._username.startsWith(AuthTokenAuthenticator.TOKEN_LOGIN_PREFIX)) {
            String token = this._username.substring(AuthTokenAuthenticator.TOKEN_LOGIN_PREFIX.length());
            comptoirPrincipal = comptoirRealm.authenticateUserToken(token);
        } else {
            comptoirPrincipal = comptoirRealm.authenticateUserLoginPassword(_username, _passwd);
        }
        String[] groups = comptoirRealm.fetchGroups(comptoirPrincipal);

        if (comptoirPrincipal == null || groups == null) {
            throw new LoginException("Login failed for " + _username);
        }

        LOG.fine("Login suceeded for " + comptoirPrincipal.toString() + " : " + Arrays.toString(groups));
        _subject.getPrincipals().add(comptoirPrincipal);
        commitUserAuthentication(groups);
    }
}
