package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.company.WsImportSummary;
import be.valuya.comptoir.ws.rest.api.domain.company.WsPrestashopImportParams;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/import")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ImportResourceApi {

    @POST
    @Path("{companyId}/{backendName}")
    @Operation(summary = "Import from Prestashop", operationId = "importPrestashop")
    WsImportSummary importItems(
            @Parameter(name = "companyId", description = "The company id", required = true)
            @PathParam("companyId") Long companyId,
            @Parameter(name = "backendName", description = "The backend name", required = true)
            @PathParam("backendName") String backendName,
            @RequestBody(required = true, description = "The parameters")
            @Valid WsPrestashopImportParams prestashopImportParams);
}
