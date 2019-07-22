package be.valuya.comptoir.ws.rest.provider;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cghislai on 11/02/17.
 */
@Provider
public class WsExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(WsExceptionMapper.class.getSimpleName());

    @Inject
    private HttpServletRequest request;

    @Override
    public Response toResponse(Exception throwable) {
        if (throwable instanceof WebApplicationException) {
            WebApplicationException applicationException = (WebApplicationException) throwable;
            return applicationException.getResponse();
        }
        LOG.log(Level.SEVERE, "Unhandled exception in Ws", throwable);
        String origin = request.getHeader("Origin");
        return Response.status(500)
                .entity(throwable.getMessage())
                .header("Access-Control-Allow-Origin", origin)
                .header("Access-Control-Allow-Credentials", "true")
                .type("text/plain")
                .build();
    }
}
