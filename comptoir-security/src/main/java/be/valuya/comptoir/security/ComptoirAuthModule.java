package be.valuya.comptoir.security;

import be.valuya.comptoir.security.authenticator.AuthTokenAuthenticator;
import be.valuya.comptoir.security.authenticator.Authenticator;
import be.valuya.comptoir.security.authenticator.LoginPasswordAuthenticator;
import be.valuya.comptoir.security.authenticator.OptionsRequestsAuthenticator;
import be.valuya.comptoir.security.credential.AuthTokenCredentialProvider;
import be.valuya.comptoir.security.credential.BasicAuthCredentialProvider;
import be.valuya.comptoir.security.credential.CredentialProvider;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cghislai on 12/02/17.
 */
public class ComptoirAuthModule implements ServerAuthModule {
    private final static Logger LOG = Logger.getLogger(ComptoirAuthModule.class.getCanonicalName());
    private static final String AUTH_TYPE_INFO_KEY = "javax.servlet.http.authType";
    public static final String AUTH_MODULE_NAME = "ComptoirSAM";
    public static final String REALM_NAME = "comptoirRealm";

    private static final Class[] SUPPORTED_MESSAGE_TYPES = new Class[]{
            HttpServletRequest.class,
            HttpServletResponse.class
    };
    private static final String[] DEFAULT_GROUPS = {"anonymous"};
    private MessagePolicy requestPolicy;
    private MessagePolicy responsePolicy;
    private CallbackHandler handler;
    private Map options;
    private ComptoirAuthModuleOptions authModuleOptions;
    private Set<CredentialProvider> credentialProviders;
    private List<Authenticator> authenticators;
    private HashSet extractedCredentials;

    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
        this.requestPolicy = requestPolicy;
        this.responsePolicy = responsePolicy;
        this.handler = handler;
        this.options = options;
        this.authModuleOptions = new ComptoirAuthModuleOptions(options);

        this.credentialProviders = new HashSet<>();
        this.credentialProviders.add(new BasicAuthCredentialProvider());
        this.credentialProviders.add(new AuthTokenCredentialProvider());

        this.authenticators = new ArrayList<>();
        this.authenticators.add(new OptionsRequestsAuthenticator(this.authModuleOptions));
        this.authenticators.add(new LoginPasswordAuthenticator());
        this.authenticators.add(new AuthTokenAuthenticator());
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        return SUPPORTED_MESSAGE_TYPES;
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        final HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        final HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();

        if (!request.isSecure() && !requestPolicy.isMandatory()) {
            return AuthStatus.SUCCESS;
        }
        if (!request.isSecure()) {
            return this.handleMandatoryAuthenticationFailure(request, response);
        }

        // Extract credential
        this.extractedCredentials = new HashSet<>();
        this.credentialProviders.stream()
                .map(provider -> provider.extractCredentials(request))
                .forEach(credOptional -> credOptional.ifPresent(extractedCredentials::add));
        clientSubject.getPrivateCredentials().addAll(extractedCredentials);

        // Attempt to authenticate using them
        AuthStatus authStatus = AuthStatus.SUCCESS;
        for (Authenticator authenticator : this.authenticators) {
            boolean hasValidCredential = authenticator.checkCredentials(clientSubject, request, response);
            boolean isAuthenticated = false;
            if (hasValidCredential) {
                this.handleAuthenticatorCallback(authenticator);
                AuthStatus authenticatorStatus = authenticator.getAuthStatus();
                if (isSuccess(authenticatorStatus)) {
                    isAuthenticated = true;
                    authStatus = authenticatorStatus;
                }
            }
            authenticator.clearAuthentication();
            if (isAuthenticated) {
                break;
            }
        }

        GroupPrincipalCallback groupPrincipalCallback = new GroupPrincipalCallback(clientSubject, DEFAULT_GROUPS);
        List<Callback> callbacks = new ArrayList<>();
        callbacks.add(groupPrincipalCallback);

        try {
            this.handler.handle(callbacks.toArray(new Callback[0]));
        } catch (IOException | UnsupportedCallbackException e) {
            AuthException authException = new AuthException();
            authException.initCause(e);
            throw authException;
        }

        // Return result
        this.debugAuthResult(authStatus, clientSubject);
        return authStatus;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        if (subject != null) {
            subject.getPrincipals().clear();
            subject.getPrivateCredentials().removeAll(this.extractedCredentials);
            this.extractedCredentials.clear();
        }
        this.authenticators.stream()
                .forEach(Authenticator::clearAuthentication);
    }

    private void handleAuthenticatorCallback(Authenticator authenticator) throws AuthException {
        try {
            authenticator.handleCallback(this.handler);
        } catch (IOException | UnsupportedCallbackException e) {
            throw new AuthException("Failed to handle callback");
        }
    }

    private AuthStatus handleMandatoryAuthenticationFailure(HttpServletRequest request, HttpServletResponse response) throws AuthException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        if (!isXmlHttpRequest(request)) {
            response.addHeader("WWW-Authenticate", "Basic realm=\"Comptoir\"");
        }
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            throw new AuthException(e.getMessage());
        }
        debug("Mandatory authentification missing");
        return AuthStatus.SEND_FAILURE;
    }


    private boolean isXmlHttpRequest(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        return "XmlHttpRequest".equals(requestedWithHeader);
    }

    private void debugAuthResult(AuthStatus authStatus, Subject subject) {
        String callbackValues = subject.getPrincipals().stream()
                .map(this::getPrincipalString)
                .reduce("", (cur, next) -> cur + " " + next, String::concat);
        debug("Authentication result: " + authStatus + " with principals : " + callbackValues);
    }

    private String getPrincipalString(Principal principal) {
        return "[ " + principal.getClass().getSimpleName() + " " + principal.getName() + " ]";
    }

    private boolean isSuccess(AuthStatus authStatus) {
        return authStatus != AuthStatus.FAILURE
                && authStatus != AuthStatus.SEND_FAILURE;
    }


    private void debug(String s) {
        if (this.authModuleOptions.isDebug() && LOG.isLoggable(Level.FINE)) {
            LOG.fine(s);
        }
    }
}
