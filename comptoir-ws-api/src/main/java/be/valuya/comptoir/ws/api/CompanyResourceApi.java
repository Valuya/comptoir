package be.valuya.comptoir.ws.api;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
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

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CompanyResourceApi {

    @POST
    @Path("")
    @Valid
    @Operation(summary = "Create a company", operationId = "createCompany")
    WsCompanyRef createCompany(
            @RequestBody(required = true, description = "The company to create")
            @NoId @Valid WsCompany wsCompany
    );

    @Path("{id}")
    @PUT
    @Valid
    @Operation(summary = "Update a company", operationId = "updateCompany")
    WsCompanyRef saveCompany(
            @Parameter(name = "id", required = true, description = "The company id")
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The updated company")
            @Valid WsCompany wsCompany
    );

    @Path("{id}")
    @Valid
    @GET
    @Operation(summary = "Get a company", operationId = "getCompany")
    WsCompany getCompany(
            @Parameter(name = "id", required = true, description = "The company id")
            @PathParam("id") Long id);

// TODO: byte[] body seems to confuse the typescript generator
//    @POST
//    @Path("{id}/import")
//    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
//    @Operation(summary = "Import company items", operationId = "importCompanyItems")
//    WsCompanyRef importItems(
//            @Parameter(name = "id", required = true, description = "The company id")
//            @PathParam("id") Long companyId,
//            @RequestBody(required = true, description = "The item to import) byte[] data);

}


