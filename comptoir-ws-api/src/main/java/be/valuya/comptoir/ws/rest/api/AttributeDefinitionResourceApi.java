package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinition;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinitionSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAttributeSearch;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/attribute/definition")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AttributeDefinitionResourceApi {

    @POST
    @Operation(summary = "Create attribute definition", operationId = "createAttributeDefinition")
    @Valid WsAttributeDefinitionRef createAttributeDefinition(
            @RequestBody(required = true, description = "The attribute definition to create")
            @NoId @Valid WsAttributeDefinition wsAttributeDefinition
    );

    @Path("{id}")
    @PUT
    @Operation(summary = "Update attribute definition", operationId = "updateAttributeDefinition")
    @Valid WsAttributeDefinitionRef updateAttributeDefinition(
            @Parameter(name = "id", description = "The attribute definition id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The attribute definition to update")
            @Valid WsAttributeDefinition wsAttributeDefinition);

    @Path("{id}")
    @GET
    @Operation(summary = "Get attribute definition", operationId = "getAttributeDefinition")
    @Valid WsAttributeDefinition getAttributeDefinition(
            @Parameter(name = "id", description = "The attribute definition id", required = true)
            @PathParam("id") long id);

    @POST
    @Path("search")
    @Operation(summary = "Search attribute definitions", operationId = "findAttributeDefinitions")
// TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @Valid WsAttributeDefinitionSearchResult findAttributeDefinitions(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(description = "The attribute definition filter", required = true)
            @Valid WsAttributeSearch wsAttributeSearch);
}
