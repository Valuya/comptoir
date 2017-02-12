package be.valuya.comptoir.security.credential;

/**
 * Created by cghislai on 12/02/17.
 */
public class AuthTokenCredential {
    private String token;

    public AuthTokenCredential(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
