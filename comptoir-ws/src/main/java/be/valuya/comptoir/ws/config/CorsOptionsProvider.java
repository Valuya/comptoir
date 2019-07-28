package be.valuya.comptoir.ws.config;

import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by cghislai on 11/02/17.
 */

@ApplicationScoped
public class CorsOptionsProvider {

    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.cors.allowedHosts", defaultValue = "*")
    private List<String> allowedOrigins;
    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.cors.allowedHeaders", defaultValue = "cache-control,content-type,accept,accept-charset,authorization,X-Requested-With,ngsw-bypass")
    private Set<String> allowedHeaders;
    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.cors.exposedHeaders", defaultValue = "content-encoding,content-length," + ApiParameters.LIST_RESULTS_COUNT_HEADER)
    private Set<String> exposedHeaders;

    @PostConstruct()
    public void init() {
    }

    public List<String> getAllowedOrigins() {
        return this.allowedOrigins;
    }

    public Set<String> getAllowedMethods() {
        return Set.of(
                HttpMethod.GET, HttpMethod.PUT,
                HttpMethod.POST, HttpMethod.DELETE
        );
    }

    public Set<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public boolean isWsCorsAllowCredentials() {
        return true;
    }

    public int getCorsMaxAge() {
        return 0;
    }

    public Set<String> getExposedHeaders() {
        return exposedHeaders;
    }
}
