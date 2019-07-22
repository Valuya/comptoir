package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPos;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPosSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsPosSearch;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/pos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PosResourceApi {

    @POST
    @Valid
    @Operation(summary = "Create new point of sale", operationId = "createPos")
    WsPosRef createPos(
            @RequestBody(description = "The point of sale", required = true)
            @NoId @Valid WsPos wsPos
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update point of sale", operationId = "updatePos")
    WsPosRef updatePos(
            @Parameter(name = "id", description = "The point of sale id", required = true)
            @PathParam("id") long id,
            @RequestBody(description = "The point of sale", required = true)
            @Valid WsPos wsPos
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get point of sale", operationId = "getPos")
    WsPos getPos(
            @Parameter(name = "id", description = "The point of sale id", required = true)
            @PathParam("id") long id
    );

    @Path("search")
    @POST
    @Operation(summary = "Search point of sales", operationId = "findPosList")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @Valid WsPosSearchResult findPosList(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(description = "The point of sale filter", required = true)
            @Valid WsPosSearch wsPosSearch
    );

}
