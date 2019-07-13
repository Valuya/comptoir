package be.valuya.comptoir.ws.security;

import be.valuya.comptoir.ws.security.credential.CredentialExtractor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CompositeHttpAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private Instance<CredentialExtractor> credentialExtractors;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        CredentialValidationResult credentialValidationResult = StreamSupport.stream(credentialExtractors.spliterator(), false)
                .map(extractor -> extractor.extractCredential(request))
                .flatMap(this::streamOptional)
                .map(identityStoreHandler::validate)
                // Stop on first extracted credential that gets an authorization result
                .filter(this::hasAuthorizationResult)
                .findFirst()
                .orElse(CredentialValidationResult.NOT_VALIDATED_RESULT);
        return this.handleValidationResult(credentialValidationResult, httpMessageContext);
    }

    private <R> Stream<R> streamOptional(Optional<R> optional) {
        return optional.map(Stream::of)
                .orElseGet(Stream::empty);
    }

    private boolean hasAuthorizationResult(CredentialValidationResult res) {
        return res.getStatus() != CredentialValidationResult.Status.NOT_VALIDATED;
    }

    private AuthenticationStatus handleValidationResult(CredentialValidationResult validationResult, HttpMessageContext httpMessageContext) {
        if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
            // Valid
            return httpMessageContext.notifyContainerAboutLogin(validationResult);
        }
        if (httpMessageContext.isProtected() || validationResult.getStatus() == CredentialValidationResult.Status.INVALID) {
            // Invalid
            HttpServletResponse response = httpMessageContext.getResponse();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            addCorsHeaders(httpMessageContext.getRequest(), response);
            addWWWAuthenticateHeaders(httpMessageContext, response);
            return AuthenticationStatus.SEND_FAILURE;
        }
        // Not validated but resource not protected
        return httpMessageContext.doNothing();
    }

    private void addWWWAuthenticateHeaders(HttpMessageContext httpMessageContext, HttpServletResponse response) {
        HttpServletRequest request = httpMessageContext.getRequest();
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean isXHR = "XmlHttpRequest".equals(requestedWithHeader);
        if (!isXHR) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Comptoir\"");
        }
    }

    private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        // FIXME: filter origin
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }


}
