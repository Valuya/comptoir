package be.valuya.wsfilter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by cghislai on 11/02/17.
 */

@ApplicationScoped
public class CorsOptionsProvider {

    private CorsOptions options;

    @PostConstruct()
    public void init() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("webServices.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            this.options = this.getOptionsFromProperties(properties);
        } catch (IOException e) {
            this.options = this.getDefaultOptions();
        }
    }

    @Produces
    public CorsOptions getOptions() {
        return this.options;
    }

    private CorsOptions getOptionsFromProperties(Properties properties) {
        CorsOptions options = this.getDefaultOptions();
        String allowedHosts = properties.getProperty("be.valuya.comptoir.ws.cors-allowed-hosts");
        Optional.ofNullable(allowedHosts).ifPresent(options::setAllowedOrigins);
        String allowAnyWithCredentialStirng = properties.getProperty("be.valuya.comptoir.ws.cors-allow-any-with-credentials");
        Optional.ofNullable(allowAnyWithCredentialStirng)
                .map(Boolean::valueOf)
                .ifPresent(options::setAllowAllWithCredentials);
        return options;
    }

    private CorsOptions getDefaultOptions() {
        CorsOptions options = new CorsOptions();
        options.setAllowAllWithCredentials(false);
        options.setAllowedOrigins("");
        return options;
    }

}
