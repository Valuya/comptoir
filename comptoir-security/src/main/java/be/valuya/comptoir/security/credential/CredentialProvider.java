package be.valuya.comptoir.security.credential;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by cghislai on 12/02/17.
 */
public interface CredentialProvider {

    Optional<? extends Object> extractCredentials(HttpServletRequest request);

}
