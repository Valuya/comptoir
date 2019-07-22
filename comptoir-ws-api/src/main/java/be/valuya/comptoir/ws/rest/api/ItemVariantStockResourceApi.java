package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantStockSearch;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStockRef;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStock;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStockSearchResult;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/itemVariantStock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ItemVariantStockResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a itemVariantStock", operationId = "createItemVariantStock")
    WsItemVariantStockRef createItemVariantStock(
            @RequestBody(required = true, description = "The itemVariantStock to create")
            @NoId @Valid WsItemVariantStock wsItemVariantStock
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a itemVariantStock", operationId = "getItemVariantStock")
    WsItemVariantStock getItemVariantStock(
            @Parameter(name = "id", description = "The itemVariantStock id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search itemVariantStocks", operationId = "searchItemVariantStocks")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsItemVariantStockSearchResult findItemVariantStocks(
            @RequestBody(required = true, description = "The itemVariantStock filter")
            @Valid WsItemVariantStockSearch wsItemVariantStockSearch
    );
}
