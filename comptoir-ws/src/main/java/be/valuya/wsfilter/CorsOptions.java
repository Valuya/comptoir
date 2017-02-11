package be.valuya.wsfilter;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
