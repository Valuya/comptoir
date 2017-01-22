package be.valuya.comptoir.ws.rest.filter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Respond with CORS headers to OPTIONS requests.
 *
 * @author cghislai
 */
@PreMatching
@Priority(Priorities.HEADER_DECORATOR)
public class CrossOriginResourceSharingRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        if (!"OPTIONS".equals(method)) {
            return;
        }
        String origin = requestContext.getHeaderString("Origin");
        Response okResponse = Response.ok()
                .header("Access-Control-Allow-Origin", origin)
                .header("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE")
                .header("Access-Control-Allow-Headers", "content-type, accept, accept-charset, authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Expose-Headers", "accept-ranges, content-encoding, content-length, X-Comptoir-ListTotalCount")
                .build();
        requestContext.abortWith(okResponse);
    }
}
