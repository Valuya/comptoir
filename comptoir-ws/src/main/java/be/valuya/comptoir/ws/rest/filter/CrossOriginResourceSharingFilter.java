package be.valuya.comptoir.ws.rest.filter;

import be.valuya.comptoir.ws.config.HeadersConfig;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author cghislai
 */
@Provider
public class CrossOriginResourceSharingFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "content-type, accept, accept-charset, authorisation");
        response.getHeaders().putSingle("Access-Control-Expose-Headers", HeadersConfig.LIST_RESULTS_COUNT_HEADER);
    }
    
}
