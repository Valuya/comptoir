package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPicture;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsPictureSearch;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/picture")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PictureResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a picture", operationId = "createPicture")
    WsPictureRef createPicture(
            @RequestBody(required = true, description = "The picture to create")
            @NoId @Valid WsPicture wsPicture
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a picture", operationId = "updatePicture")
    WsPictureRef updatePicture(
            @Parameter(name = "id", description = "The picture id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The picture to update")
            @Valid WsPicture wsPicture
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a picture", operationId = "getPicture")
    WsPicture getPicture(
            @Parameter(name = "id", description = "The picture id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search pictures", operationId = "searchPictures")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsPictureSearchResult findPictures(
            @RequestBody(required = true, description = "The picture filter")
            @Valid WsPictureSearch wsPictureSearch
    );

}
