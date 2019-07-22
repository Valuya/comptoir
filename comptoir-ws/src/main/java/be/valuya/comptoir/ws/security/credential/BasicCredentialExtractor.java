package be.valuya.comptoir.ws.security.credential;

import be.valuya.comptoir.util.Base64Utils;
import be.valuya.comptoir.util.HeaderUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class BasicCredentialExtractor implements CredentialExtractor {

    private static final Pattern BASIC_AUTH_HEADER_PATTERN = Pattern.compile("Basic\\s+([a-zA-Z0-9._\\-~+=/]+)$");
    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String AUTHORIZATION_REQUEST_PARAM_NAME = "basic_auth";

    @Override
    public Optional<Credential> extractCredential(HttpServletRequest servletRequest) {
        return this.getBasicAuthValueOptional(servletRequest)
                .flatMap(this::extractBasicCredential);
    }

    private Optional<Credential> extractBasicCredential(String basicCredentialValue) {
        String decoded = Base64Utils.decodeBase64(basicCredentialValue);

        int colonIndex = decoded.indexOf(':');
        if (colonIndex <= 0 || colonIndex == decoded.length() - 1) {
            return Optional.empty();
        }

        String username = decoded.substring(0, colonIndex);
        String password = decoded.substring(colonIndex + 1);

        UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
        return Optional.of(credential);
    }

    private Optional<String> getBasicAuthValueOptional(HttpServletRequest servletRequest) {
        return getBasicAuthHeaderValueOptional(servletRequest)
                .map(Optional::of)
                .orElseGet(() -> this.getBasicAuthRequestValueOptional(servletRequest));
    }

    private Optional<String> getBasicAuthRequestValueOptional(HttpServletRequest servletRequest) {
        String requestParamValueOptional = servletRequest.getParameter(AUTHORIZATION_REQUEST_PARAM_NAME);
        return Optional.ofNullable(requestParamValueOptional);
    }

    private Optional<String> getBasicAuthHeaderValueOptional(HttpServletRequest servletRequest) {
        return HeaderUtils.getHeaderIgnoreCase(AUTHORIZATION_HEADER, servletRequest)
                .flatMap(this::extractBasicAuthorizationValue);
    }

    private Optional<String> extractBasicAuthorizationValue(String headerValue) {
        Matcher matcher = BASIC_AUTH_HEADER_PATTERN.matcher(headerValue);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        String authHeaderValueNullable = matcher.group(1);
        return Optional.ofNullable(authHeaderValueNullable);
    }
}
