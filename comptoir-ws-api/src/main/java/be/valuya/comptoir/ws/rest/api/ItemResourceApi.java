package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItem;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemSearch;
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

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ItemResourceApi {



    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a item", operationId = "createItem")
    WsItemRef createItem(
            @RequestBody(required = true, description = "The item to create")
            @NoId @Valid WsItem wsItem
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a item", operationId = "updateItem")
    WsItemRef updateItem(
            @Parameter(name = "id", description = "The item id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The item to update")
            @Valid WsItem wsItem
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a item", operationId = "getItem")
    WsItem getItem(
            @Parameter(name = "id", description = "The item id", required = true)
            @PathParam("id") long id
    );


    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a item", operationId = "deleteItem")
    void deleteItem(
            @Parameter(name = "id", description = "The item id", required = true)
            @PathParam("id") long id);

    @POST
    @Path("search")
    @Operation(summary = "Search items", operationId = "findItems")
// TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @Valid WsItemSearchResult findItems(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(description = "The item filter", required = true)
            @Valid WsItemSearch wsItemSearch);
}
