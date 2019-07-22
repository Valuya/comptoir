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
public class WsApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOG = Logger.getLogger(WsApplicationExceptionMapper.class.getSimpleName());

    @Inject
    private HttpServletRequest request;

    @Override
    public Response toResponse(WebApplicationException throwable) {
        int status = throwable.getResponse().getStatus();
        LOG.log(Level.SEVERE, "Unhandled exception in Ws", throwable);
        String origin = request.getHeader("Origin");

        return Response.status(status)
                .entity(throwable.getMessage())
                .header("Access-Control-Allow-Origin", origin)
                .header("Access-Control-Allow-Credentials", "true")
                .type("text/plain")
                .build();
    }
}
