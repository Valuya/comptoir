package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoice;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoiceRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsInvoiceSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsInvoiceSearch;
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

@Path("/invoice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InvoiceResourceApi {


    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a invoice", operationId = "createInvoice")
    WsInvoiceRef createInvoice(
            @RequestBody(required = true, description = "The invoice to create")
            @NoId @Valid WsInvoice wsInvoice
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a invoice", operationId = "updateInvoice")
    WsInvoiceRef updateInvoice(
            @Parameter(name = "id", description = "The invoice id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The invoice to update")
            @Valid WsInvoice wsInvoice
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get a invoice", operationId = "getInvoice")
    WsInvoice getInvoice(
            @Parameter(name = "id", description = "The invoice id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search invoices", operationId = "searchInvoices")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsInvoiceSearchResult findInvoices(
            @RequestBody(required = true, description = "The invoice filter")
            @Valid WsInvoiceSearch wsInvoiceSearch
    );

}
