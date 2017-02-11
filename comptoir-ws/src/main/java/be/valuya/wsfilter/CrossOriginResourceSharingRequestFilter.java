package be.valuya.wsfilter;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Respond with CORS headers to OPTIONS requests.
 *
 * @author cghislai
 */
@PreMatching
@Priority(Priorities.HEADER_DECORATOR)
public class CrossOriginResourceSharingRequestFilter implements ContainerRequestFilter {

    @Inject
    private CorsOptionsProvider corsOriginProvider;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        if (!"OPTIONS".equals(method)) {
            return;
        }
        CorsOptions corsOptions = corsOriginProvider.getOptions();
        String allowedOrigin = this.getAllowedOriginFromContext(requestContext, corsOptions);

        Response okResponse = Response.ok()
                .header("Access-Control-Allow-Origin", allowedOrigin)
                .header("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE")
                .header("Access-Control-Allow-Headers", "content-type, accept, accept-charset, authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Expose-Headers", "accept-ranges, content-encoding, content-length, X-Comptoir-ListTotalCount")
                .build();
        requestContext.abortWith(okResponse);
    }

    private String getAllowedOriginFromContext(ContainerRequestContext requestContext, CorsOptions corsOptions) {
        boolean withCredentials =
                Optional.ofNullable(requestContext.getHeaderString("access-control-request-headers"))
                        .map(header -> header.split(","))
                        .map(Arrays::asList)
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(header -> header.trim())
                        .anyMatch(header -> header.equalsIgnoreCase("authorization"));
        boolean allowAllWithCredentials = corsOptions.isAllowAllWithCredentials();
        String allowedOrigin = corsOptions.getAllowedOrigins();

        if (withCredentials && allowAllWithCredentials && allowedOrigin.equalsIgnoreCase("*")) {
            String requestOrigin = requestContext.getHeaderString("Origin");
            allowedOrigin = requestOrigin;
        }
        return allowedOrigin;
    }
}
