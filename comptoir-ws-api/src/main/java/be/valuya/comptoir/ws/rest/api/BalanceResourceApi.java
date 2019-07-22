package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalance;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsBalanceSearch;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/balance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BalanceResourceApi {


    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a balance", operationId = "createBalance")
    WsBalanceRef createBalance(
            @RequestBody(required = true, description = "The balance to create")
            @NoId @Valid WsBalance wsBalance
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a balance", operationId = "updateBalance")
    WsBalanceRef updateBalance(
            @Parameter(name = "id", description = "The balance id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The balance to update")
            @Valid WsBalance wsBalance
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a balance", operationId = "getBalance")
    WsBalance getBalance(
            @Parameter(name = "id", description = "The balance id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search balances", operationId = "searchBalances")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsBalanceSearchResult findBalances(
            @RequestBody(required = true, description = "The balance filter")
            @Valid WsBalanceSearch wsBalanceSearch
    );

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a balance", operationId = "deleteBalance")
    void deleteBalance(
            @Parameter(name = "id", description = "The balance id", required = true)
            @PathParam("id") long id);

    @PUT
    @Valid
    @Path("{id}/state/CLOSED")
    @Operation(summary = "Close a balance", operationId = "closeBalance")
    WsBalanceRef closeBalance(
            @Parameter(name = "id", description = "The balance id", required = true)
            @PathParam("id") long id);
}
