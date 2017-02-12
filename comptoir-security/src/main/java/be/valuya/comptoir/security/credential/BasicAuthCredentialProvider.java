package be.valuya.comptoir.security.credential;

import javax.resource.spi.security.PasswordCredential;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cghislai on 12/02/17.
 */
public class BasicAuthCredentialProvider implements CredentialProvider {

    @Override
    public Optional<PasswordCredential> extractCredentials(HttpServletRequest request) {
        return this.getBasicAuthParts(request)
                .map(parts -> {
                    String login = parts[0];
                    String password = parts[1];
                    return new PasswordCredential(login, password.toCharArray());
                });
    }

    private Optional<String[]> getBasicAuthParts(HttpServletRequest servletRequest) {
        String authorizationHeader = servletRequest.getHeader("authorization");
        Pattern pattern = Pattern.compile("^Basic (.*)$");
        return Optional.ofNullable(authorizationHeader)
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .map(this::base64DecodeString)
                .map(decoded -> decoded.split(":"));
    }

    private String base64DecodeString(String encoded) {
        byte[] decoded = Base64.getDecoder().decode(encoded);
        try {
            return new String(decoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
