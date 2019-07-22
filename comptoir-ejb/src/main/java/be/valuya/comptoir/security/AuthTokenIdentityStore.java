package be.valuya.comptoir.security;

import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.service.EmployeeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

@ApplicationScoped
public class AuthTokenIdentityStore implements IdentityStore {

    @Inject
    private AuthService authService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof AuthTokenCredential) {
            AuthTokenCredential authTokenCredential = (AuthTokenCredential) credential;
            return this.validate(authTokenCredential);
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    private CredentialValidationResult validate(AuthTokenCredential tokenCredential) {
        String token = tokenCredential.getToken();

        return authService.validateTokenAuth(token);
    }
}
