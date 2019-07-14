package be.valuya.comptoir.ws.api;

import be.valuya.comptoir.api.domain.commercial.WsPos;
import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.search.WsPosSearch;
import be.valuya.comptoir.ws.api.parameters.ApiParameters;
import be.valuya.comptoir.ws.api.parameters.PaginationParams;
import be.valuya.comptoir.ws.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
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
    @Operation(summary = "Create new point of sale", operationId = "createPointOfSale")
    WsPosRef createPos(
            @RequestBody(description = "The point of sale", required = true)
            @NoId @Valid WsPos wsPos
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update point of sale", operationId = "updatePointOfSale")
    WsPosRef savePos(
            @Parameter(name = "id", description = "The point of sale id", required = true)
            @PathParam("id") long id,
            @RequestBody(description = "The point of sale", required = true)
            @Valid WsPos wsPos
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get point of sale", operationId = "getPointOfSale")
    WsPos getPos(
            @Parameter(name = "id", description = "The point of sale id", required = true)
            @PathParam("id") long id
    );

    @Path("search")
    @POST
    @Valid
    @Operation(summary = "Search point of sales", operationId = "searchPointOfSales")
// TODO: produces invalid output: See https://github.com/eclipse/microprofile-open-api/issues/362
//    @APIResponses({@APIResponse(
//            headers = {@Header(name = ApiParameters.LIST_RESULTS_COUNT_HEADER,
//                    schema = @Schema(type = SchemaType.INTEGER),
//                    description = "The amount of point of sales matching the filter")},
//            content = @Content(schema = @Schema(description = "The search result page",
//                    type = SchemaType.ARRAY,
//                    implementation = WsPos.class)),
//            description = "Search results"
//    )})
    List<WsPos> findPosList(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(description = "The point of sale filter", required = true)
            @Valid WsPosSearch wsPosSearch
    );

}
