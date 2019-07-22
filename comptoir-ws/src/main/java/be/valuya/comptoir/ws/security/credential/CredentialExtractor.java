package be.valuya.comptoir.ws.security.credential;

import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface CredentialExtractor {

    Optional<Credential> extractCredential(HttpServletRequest servletRequest);
}
