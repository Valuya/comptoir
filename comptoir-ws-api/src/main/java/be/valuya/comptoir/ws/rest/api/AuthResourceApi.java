package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.auth.WsAuth;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed(ComptoirRoles.ACTIVE)
public interface AuthResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Login", operationId = "login")
    WsAuth login(
            @Parameter(name = "Authorization", description = "The basic credentials", required = true)
            @HeaderParam("Authorization") String authorizationHeader);

    @POST
    @Path("/refresh/{refreshToken}")
    @Valid
    @Operation(summary = "Refresh token", operationId = "refreshAuth")
    WsAuth refreshAuth(
            @Parameter(name = "refreshToken", description = "The refresh token", required = true)
            @PathParam("refreshToken") String refreshToken
    );
}
