package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntrySearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAccountingEntrySearch;
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

@Path("/accountingEntry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountingEntryResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create an accounting entry", operationId = "createAccountingEntry")
    WsAccountingEntryRef createAccountingEntry(
            @RequestBody(required = true, description = "The accounting entry to create")
            @NoId @Valid WsAccountingEntry wsAccountingEntry
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update an accounting entry", operationId = "updateAccountingEntry")
    WsAccountingEntryRef updateAccountingEntry(
            @Parameter(name = "id", description = "The accounting entry id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The accounting entry to update")
            @Valid WsAccountingEntry wsAccountingEntry
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get an accounting entry", operationId = "getAccountingEntry")
    WsAccountingEntry getAccountingEntry(
            @Parameter(name = "id", description = "The accounting entry id", required = true)
            @PathParam("id") long id
    );

    @Path("{id}")
    @DELETE
    @Operation(summary = "Delete an accounting entry", operationId = "deleteAccountingEntry")
    void deleteAccountingEntry(
            @Parameter(name = "id", description = "The accounting entry id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search accounting entries", operationId = "searchAccountingEntries")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    WsAccountingEntrySearchResult findAccountingEntries(
            @RequestBody(required = true, description = "The accounting entry filter")
            @Valid WsAccountingEntrySearch wsAccountingEntrySearch
    );

}
