package be.valuya.comptoir.ws.api;

import be.valuya.comptoir.api.domain.accounting.WsAccount;
import be.valuya.comptoir.api.domain.accounting.WsAccountRef;
import be.valuya.comptoir.api.domain.search.WsAccountSearch;
import be.valuya.comptoir.ws.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
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

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create an account", operationId = "createAccount")
    WsAccountRef createAccount(
            @RequestBody(required = true, description = "The account to create")
            @NoId @Valid WsAccount wsAccount
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update an account", operationId = "updateAccount")
    WsAccountRef saveAccount(
            @Parameter(name = "id", description = "The account id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The account to update")
            @Valid WsAccount wsAccount
    );

    @Path("{id}")
    @GET
    @Valid
    @Operation(summary = "Get an account", operationId = "getAccount")
    WsAccount getAccount(
            @Parameter(name = "id", description = "The account id", required = true)
            @PathParam("id") long id
    );

    @POST
    @Path("search")
    @Valid
    @Operation(summary = "Search accounts", operationId = "searchAccounts")
    List<WsAccount> findAccounts(
            @RequestBody(required = true, description = "The account filter")
            @Valid WsAccountSearch wsAccountSearch
    );

}
