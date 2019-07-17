package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantSaleSearch;
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

@Path("/itemVariantSale")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ItemVariantSaleResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a itemVariantSale", operationId = "createItemVariantSale")
    WsItemVariantSaleRef createItemVariantSale(
            @RequestBody(required = true, description = "The itemVariantSale to create")
            @NoId @Valid WsItemVariantSale wsItemVariantSale
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a itemVariantSale", operationId = "updateItemVariantSale")
    WsItemVariantSaleRef updateItemVariantSale(
            @Parameter(name = "id", description = "The itemVariantSale id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The itemVariantSale to update")
            @Valid WsItemVariantSale wsItemVariantSale
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a itemVariantSale", operationId = "getItemVariantSale")
    WsItemVariantSale getItemVariantSale(
            @Parameter(name = "id", description = "The itemVariantSale id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search itemVariantSales", operationId = "searchItemVariantSales")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsItemVariantSaleSearchResult findItemVariantSales(
            @RequestBody(required = true, description = "The itemVariantSale filter")
            @Valid WsItemVariantSaleSearch wsItemVariantSaleSearch
    );

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a itemVariantSale", operationId = "deleteItemVariantSale")
    void deleteItemVariantSale(
            @Parameter(name = "id", description = "The itemVariantSale id", required = true)
            @PathParam("id") long id);

}
