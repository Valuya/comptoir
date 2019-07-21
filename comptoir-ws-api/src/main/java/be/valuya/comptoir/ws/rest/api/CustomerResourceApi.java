package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.search.WsCustomerSearch;
import be.valuya.comptoir.ws.rest.api.domain.search.WsCustomerSearch;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomer;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerSearchResult;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

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
import java.math.BigDecimal;
import java.util.List;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CustomerResourceApi {


    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a customer", operationId = "createCustomer")
    WsCustomerRef createCustomer(
            @RequestBody(required = true, description = "The customer to create")
            @NoId @Valid WsCustomer wsCustomer
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a customer", operationId = "updateCustomer")
    WsCustomerRef updateCustomer(
            @Parameter(name = "id", description = "The customer id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The customer to update")
            @Valid WsCustomer wsCustomer
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a customer", operationId = "getCustomer")
    WsCustomer getCustomer(
            @Parameter(name = "id", description = "The customer id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search customers", operationId = "searchCustomers")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
//    @APIResponse(responseCode = "200",
//            description = "OK",
//            content = {@Content(schema = @Schema(
//                    type = SchemaType.ARRAY,
//                    implementation = WsCustomer.class
//            ))})
    WsCustomerSearchResult findCustomers(
            @RequestBody(required = true, description = "The customer filter")
            @Valid WsCustomerSearch wsCustomerSearch
    );


}
