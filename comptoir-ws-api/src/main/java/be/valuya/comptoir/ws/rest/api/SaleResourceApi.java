package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalePrice;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalesSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.naming.directory.SearchResult;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
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

@Path("/sale")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SaleResourceApi {

    @POST
    @Operation(operationId = "createSale", description = "Create a sale")
    @Valid WsSaleRef createSale(
            @RequestBody(required = true, description = "sale to create")
            @NoId @Valid @NotNull WsSale wsSale
    );

    @Path("{id}")
    @PUT
    @Operation(operationId = "updateSale", description = "Update a sale")
    @Valid WsSaleRef updateSale(
            @Parameter(description = "The sale id", name = "id")
            @PathParam("id") long id,
            @RequestBody(required = true, description = "sale to update")
            @Valid WsSale wsSale
    );

    @Path("{id}")
    @GET
    @Operation(operationId = "getSale", description = "Get a sale")
    @Valid WsSale getSale(
            @Parameter(description = "The sale id", name = "id")
            @PathParam("id") long id);

    @POST
    @Path("search")
    @Operation(operationId = "findSales", description = "Search sales")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @Valid WsSalesSearchResult findSales(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(required = true, description = "search filter")
            @NotNull @Valid WsSaleSearch wsSaleSearch
    );

    @POST
    @Path("searchTotalPayed")
    @Operation(operationId = "findSalesTotalPayed", description = "Search sales")
    @Valid WsSalePrice findSalesTotalPayed(
            @RequestBody(required = true, description = "search filter")
            @Valid WsSaleSearch wsSaleSearch);

    @DELETE
    @Path("{id}")
    @Operation(operationId = "deleteSale", description = "Delete a sale")
    void deleteSale(
            @Parameter(description = "The sale id", name = "id")
            @PathParam("id") long id);

    @PUT
    @Path("{id}/state/CLOSED")
    @Operation(operationId = "closeSale", description = "Close a sale")
    @Valid WsSaleRef closeSale(
            @Parameter(description = "The sale id", name = "id")
            @PathParam("id") long id);

    @PUT
    @Path("{id}/state/OPEN")
    @Operation(operationId = "openSale", description = "Open a sale")
    @Valid WsSaleRef openSale(
            @Parameter(description = "The sale id", name = "id")
            @PathParam("id") long id);

    @GET
    @Path("{id}/payed")
    @Operation(operationId = "getSaleTotalPayed", description = "Get total paid on a sale")
    String getSaleTotalPayed(
            @Parameter(description = "The sale id", name = "id")
            @PathParam("id") long id);
}
