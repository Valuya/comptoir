package be.valuya.comptoir.ws.api;

import be.valuya.comptoir.api.domain.auth.WsAuth;
import be.valuya.comptoir.security.ComptoirRoles;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Login", operationId = "login")
    WsAuth login();

    @POST
    @Path("/refresh/{refreshToken}")
    @Valid
    @Operation(summary = "Refresh token", operationId = "refreshAuth")
    WsAuth refreshAuth(
            @Parameter(name = "refreshToken", description = "The refresh token", required = true)
            @PathParam("refreshToken") String refreshToken
    );
}
