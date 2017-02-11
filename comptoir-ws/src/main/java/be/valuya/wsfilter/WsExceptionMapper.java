package be.valuya.wsfilter;

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
public class WsExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(WsExceptionMapper.class.getSimpleName());

    @Override
    public Response toResponse(Throwable throwable) {
        if (throwable instanceof WebApplicationException) {
            WebApplicationException applicationException = (WebApplicationException) throwable;
            return applicationException.getResponse();
        }
        LOG.log(Level.SEVERE, "Unhandled exception in Ws", throwable);
        return Response.status(500).
                entity(throwable.getMessage()).
                type("text/plain").
                build();
    }
}
