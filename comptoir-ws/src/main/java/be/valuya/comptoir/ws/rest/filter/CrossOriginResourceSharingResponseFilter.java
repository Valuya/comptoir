package be.valuya.comptoir.ws.rest.filter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * Append CORS headers to all responses.
 *
 * @author cghislai
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CrossOriginResourceSharingResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        String origin = requestContext.getHeaderString("Origin");
        // FIXME: filter origin
        MultivaluedMap<String, Object> headerMap = response.getHeaders();
        headerMap.putSingle("Access-Control-Allow-Origin", origin);
        headerMap.putSingle("Access-Control-Allow-Credentials", "true");
        headerMap.putSingle("Access-Control-Expose-Headers", "X-Gestemps-ResultCount");
    }

}
