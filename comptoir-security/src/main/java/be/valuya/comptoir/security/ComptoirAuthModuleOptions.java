package be.valuya.comptoir.security;

import java.util.Map;

/**
 * Created by cghislai on 12/02/17.
 */
public class ComptoirAuthModuleOptions {
    private final static String PARAM_ALLOWED_CORS_ORIGIN = "allowed-cors-origin";
    private final static String PARAM_ALLOW_ANY_CORS_ORIGIN = "allow-any-cors-origin";
    private final static String PARAM_DEBUG = "debug";

    private final String allowedCorsOrigin;
    private final boolean allowAnyCorsOrigin;
    private final boolean debug;

    public ComptoirAuthModuleOptions(Map<String, String> optionsMap) {
        this.allowedCorsOrigin = optionsMap.getOrDefault(PARAM_ALLOWED_CORS_ORIGIN, "");
        this.allowAnyCorsOrigin = optionsMap.getOrDefault(PARAM_ALLOW_ANY_CORS_ORIGIN, "false").equals("true");
        this.debug = optionsMap.getOrDefault(PARAM_DEBUG, "false").equals("true");
    }

    public String getAllowedCorsOrigin() {
        return allowedCorsOrigin;
    }

    public boolean isAllowAnyCorsOrigin() {
        return allowAnyCorsOrigin;
    }

    public boolean isDebug() {
        return debug;
    }
}
