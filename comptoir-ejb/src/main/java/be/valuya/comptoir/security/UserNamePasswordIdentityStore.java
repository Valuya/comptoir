package be.valuya.comptoir.security;

import be.valuya.comptoir.service.AuthService;
import be.valuya.comptoir.service.EmployeeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

@ApplicationScoped
public class UserNamePasswordIdentityStore implements IdentityStore {

    @Inject
    private EmployeeService employeeService;
    @Inject
    private AuthService authService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            return this.validate(usernamePasswordCredential);
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    private CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {
        String username = usernamePasswordCredential.getCaller();
        String password = usernamePasswordCredential.getPasswordAsString();
        String hashPassword = authService.hashPassword(password);

        return employeeService.findEmployeeOptionalByLogin(username)
                .map(employee -> this.authService.validateAuth(employee, hashPassword))
                .orElse(CredentialValidationResult.NOT_VALIDATED_RESULT);
    }
}
