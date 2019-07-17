package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantSearch;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.validation.Valid;
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

@Path("/itemVariant")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ItemVariantResourceApi {
    @POST
    @Operation(summary = "Create item variant", operationId = "createItemVariant")
    @Valid WsItemVariantRef createItemVariant(
            @RequestBody(description = "The item variant", required = true)
            @NoId @Valid WsItemVariant wsItem);

    @Path("{id}")
    @PUT
    @Operation(summary = "Update item variant", operationId = "updateItemVariant")
    @Valid WsItemVariantRef updateItemVariant(@PathParam("id") long id,
                                              @RequestBody(description = "The item variant", required = true)
                                              @Valid WsItemVariant wsItem);

    @Path("{id}")
    @GET
    @Operation(summary = "Get item variant", operationId = "getItemVariant")
    @Valid WsItemVariant getItemVariant(@PathParam("id") long id);

    @Path("{id}")
    @DELETE
    @Operation(summary = "Delete item variant", operationId = "deleteItemVariant")
    void deleteItemVariant(@PathParam("id") long id);

    @POST
    @Path("search")
    @Operation(summary = "Search item variants", operationId = "findItemVariants")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @Valid WsItemVariantSearchResult findItemVariants(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(description = "The item variant filter", required = true)
            @Valid WsItemVariantSearch wsItemVariantSearch);
}
