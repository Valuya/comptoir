package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.search.WsStockSearch;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStock;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockSearchResult;
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

@Path("/stock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StockResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a stock", operationId = "createStock")
    WsStockRef createStock(
            @RequestBody(required = true, description = "The stock to create")
            @NoId @Valid WsStock wsStock
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a stock", operationId = "updateStock")
    WsStockRef updateStock(
            @Parameter(name = "id", description = "The stock id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The stock to update")
            @Valid WsStock wsStock
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a stock", operationId = "getStock")
    WsStock getStock(
            @Parameter(name = "id", description = "The stock id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search stocks", operationId = "searchStocks")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsStockSearchResult findStocks(
            @RequestBody(required = true, description = "The stock filter")
            @Valid WsStockSearch wsStockSearch
    );

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a stock", operationId = "deleteStock")
    void deleteStock(
            @Parameter(name = "id", description = "The stock id", required = true)
            @PathParam("id") long id);

}
