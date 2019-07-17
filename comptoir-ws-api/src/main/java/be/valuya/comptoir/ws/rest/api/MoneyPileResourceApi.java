package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPile;
import be.valuya.comptoir.ws.rest.api.domain.cash.WsMoneyPileRef;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/moneyPile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MoneyPileResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a moneyPile", operationId = "createMoneyPile")
    WsMoneyPileRef createMoneyPile(
            @RequestBody(required = true, description = "The moneyPile to create")
            @NoId @Valid WsMoneyPile wsMoneyPile
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a moneyPile", operationId = "updateMoneyPile")
    WsMoneyPileRef updateMoneyPile(
            @Parameter(name = "id", description = "The moneyPile id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The moneyPile to update")
            @Valid WsMoneyPile wsMoneyPile
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a moneyPile", operationId = "getMoneyPile")
    WsMoneyPile getMoneyPile(
            @Parameter(name = "id", description = "The moneyPile id", required = true)
            @PathParam("id") long id
    );
}
