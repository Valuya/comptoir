package be.valuya.comptoir.ws.security.credential;

import javax.security.enterprise.credential.Credential;

public class AuthTokenCredential implements Credential {

    private final String token;

    public AuthTokenCredential(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
