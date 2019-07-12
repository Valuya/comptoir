package be.valuya.comptoir.ws.security.credential;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class BearerCredentialExtractor implements CredentialExtractor {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final Pattern AUTH_HEADER_PATTERN = Pattern.compile("^Bearer\\s+([a-zA-Z0-9._\\-~+=/]+)$");
    private static final String AUTH_TOKEN_REQUEST_PARAM_NAME = "oauth2_token";

    @Override
    public Optional<Credential> extractCredential(HttpServletRequest servletRequest) {
        return this.getEncodedAuthTokenOptional(servletRequest)
                .map(this::decodeAuthToken);
    }

    private Credential decodeAuthToken(String encodedToken) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(encodedToken);
        String token = new String(decodedBytes, StandardCharsets.UTF_8);
        return new AuthTokenCredential(token);
    }

    private Optional<String> getEncodedAuthTokenOptional(HttpServletRequest servletRequest) {
        return getAuthHeaderValueOptional(servletRequest)
                .map(Optional::of)
                .orElseGet(() -> this.getAuthRequestValueOptional(servletRequest));
    }

    private Optional<String> getAuthHeaderValueOptional(HttpServletRequest servletRequest) {
        String authHeaderValueNullable = servletRequest.getHeader(AUTH_HEADER_NAME);
        return Optional.ofNullable(authHeaderValueNullable)
                .flatMap(this::extractAuthHeaderToken);
    }

    private Optional<String> getAuthRequestValueOptional(HttpServletRequest servletRequest) {
        String requestParameterNullable = servletRequest.getParameter(AUTH_TOKEN_REQUEST_PARAM_NAME);
        return Optional.ofNullable(requestParameterNullable);
    }

    private Optional<String> extractAuthHeaderToken(String authHeaderValue) {
        Matcher matcher = AUTH_HEADER_PATTERN.matcher(authHeaderValue);
        if (matcher.matches()) {
            String authHeaderValueNullable = matcher.group(1);
            return Optional.ofNullable(authHeaderValueNullable);
        }
        return Optional.empty();
    }
}
