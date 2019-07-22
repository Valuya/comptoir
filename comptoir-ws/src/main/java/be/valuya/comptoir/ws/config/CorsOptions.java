package be.valuya.comptoir.ws.config;

/**
 * Created by cghislai on 11/02/17.
 */
public class CorsOptions {
    private String allowedOrigins;
    private boolean allowAllWithCredentials;


    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public boolean isAllowAllWithCredentials() {
        return allowAllWithCredentials;
    }

    public void setAllowAllWithCredentials(boolean allowAllWithCredentials) {
        this.allowAllWithCredentials = allowAllWithCredentials;
    }
}
