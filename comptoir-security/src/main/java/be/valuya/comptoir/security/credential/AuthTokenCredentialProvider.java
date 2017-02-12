package be.valuya.comptoir.security.credential;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cghislai on 12/02/17.
 */
public class AuthTokenCredentialProvider implements CredentialProvider {

    @Override
    public Optional<AuthTokenCredential> extractCredentials(HttpServletRequest request) {
        return this.getAuthToken(request)
                .map(token -> new AuthTokenCredential(token));
    }

    private Optional<String> getAuthToken(HttpServletRequest servletRequest) {
        String authorizationHeader = servletRequest.getHeader("authorization");
        Pattern pattern = Pattern.compile("^Bearer (.*)$");
        return Optional.ofNullable(authorizationHeader)
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1));
    }

}
