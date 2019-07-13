package be.valuya.comptoir.ws.rest.provider;

import be.valuya.comptoir.ws.config.CorsOptions;
import be.valuya.comptoir.ws.config.CorsOptionsProvider;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

/**
 * Append CORS headers to all responses.
 *
 * @author cghislai
 */
@Provider
public class CrossOriginResourceSharingResponseFilter implements ContainerResponseFilter {

    @Inject
    private CorsOptionsProvider corsOriginProvider;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        String method = requestContext.getMethod();
        if ("OPTIONS".equals(method)) {
            return;
        }
        CorsOptions corsOptions = corsOriginProvider.getOptions();
        String allowedOrigin = this.getAllowedOriginFromContext(requestContext, corsOptions);

        MultivaluedMap<String, Object> headerMap = response.getHeaders();
        headerMap.putSingle("Access-Control-Allow-Origin", allowedOrigin);
        headerMap.putSingle("Access-Control-Allow-Credentials", "true");
        headerMap.putSingle("Access-Control-Expose-Headers", "X-Comptoir-ListTotalCount");
    }


    private String getAllowedOriginFromContext(ContainerRequestContext requestContext, CorsOptions corsOptions) {
        boolean withCredentials =
                Optional.ofNullable(requestContext.getHeaderString("authorization"))
                        .isPresent();
        boolean allowAllWithCredentials = corsOptions.isAllowAllWithCredentials();
        String allowedOrigin = corsOptions.getAllowedOrigins();

        if (withCredentials && allowAllWithCredentials && allowedOrigin.equalsIgnoreCase("*")) {
            String requestOrigin = requestContext.getHeaderString("Origin");
            allowedOrigin = requestOrigin;
        }
        return allowedOrigin;
    }
}
