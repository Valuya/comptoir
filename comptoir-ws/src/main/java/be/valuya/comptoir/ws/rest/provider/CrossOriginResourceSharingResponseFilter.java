package be.valuya.comptoir.ws.rest.provider;

import be.valuya.comptoir.ws.config.CorsOptionsProvider;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Set;

/**
 * Append CORS headers to responses:
 * - allow-origin
 * - allow-credential
 * - exposed-headers
 *
 * @author cghislai
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CrossOriginResourceSharingResponseFilter implements ContainerResponseFilter {

    @Inject
    private CorsOptionsProvider corsOriginProvider;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();

        // Only append CORS headers if the client sent its Origin header and it is one allowed
        CorsUtils.getRequestOrigin(headers)
                .ifPresent(origin -> this.appendCorsReponseHeaders(origin, requestContext, response));

    }

    private void appendCorsReponseHeaders(String origin, ContainerRequestContext requestContext, ContainerResponseContext response) {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();

        List<String> wsCorsAllowedOrigins = corsOriginProvider.getAllowedOrigins();
        boolean originAllowed = CorsUtils.isAllowedOrigin(headers, wsCorsAllowedOrigins);
        if (!originAllowed) {
            abortWitError(requestContext);
            return;
        }

        boolean wsCorsAllowCredentials = corsOriginProvider.isWsCorsAllowCredentials();
        Set<String> exposedHeaders = corsOriginProvider.getExposedHeaders();
        String exposedHeadersResponseHeader = String.join(",", exposedHeaders);

        MultivaluedMap<String, Object> headerMap = response.getHeaders();
        headerMap.putSingle("Access-Control-Allow-Origin", origin);
        headerMap.putSingle("Access-Control-Expose-Headers", exposedHeadersResponseHeader);

        if (wsCorsAllowCredentials) {
            headerMap.putSingle("Access-Control-Allow-Credentials", "true");
        }
    }

    private void abortWitError(ContainerRequestContext requestContext) {
        Response response = Response.status(Response.Status.NOT_ACCEPTABLE)
                .build();
        requestContext.abortWith(response);
    }

}
