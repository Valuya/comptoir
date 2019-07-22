package be.valuya.comptoir.ws.rest.provider;

import be.valuya.comptoir.security.UnauthenticatedException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException> {

    @Inject
    private HttpServletRequest request;

    @Override
    public Response toResponse(UnauthenticatedException exception) {
        String origin = request.getHeader("Origin");
        return Response.status(401)
                .header("Access-Control-Allow-Origin", origin)
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }
}
