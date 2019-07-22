package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.search.WsEmployeeSearch;
import be.valuya.comptoir.ws.rest.api.domain.search.WsEmployeeSearch;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeSearchResult;
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

@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EmployeeResourceApi {


    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a employee", operationId = "createEmployee")
    WsEmployeeRef createEmployee(
            @RequestBody(required = true, description = "The employee to create")
            @NoId @Valid WsEmployee wsEmployee
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a employee", operationId = "updateEmployee")
    WsEmployeeRef updateEmployee(
            @Parameter(name = "id", description = "The employee id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The employee to update")
            @Valid WsEmployee wsEmployee
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a employee", operationId = "getEmployee")
    WsEmployee getEmployee(
            @Parameter(name = "id", description = "The employee id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search employees", operationId = "searchEmployees")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsEmployeeSearchResult findEmployees(
            @RequestBody(required = true, description = "The employee filter")
            @Valid WsEmployeeSearch wsEmployeeSearch
    );

}
