package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValue;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValueSearchResult;
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

@Path("/attribute/value")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AttributeValueResourceApi {

    @POST
    @Operation(summary = "Create attribute value", operationId = "createAttributeValue")
    @Valid WsAttributeValueRef createAttributeValue(
            @RequestBody(required = true, description = "The attribute value to create")
            @NoId @Valid WsAttributeValue wsAttributeValue
    );

    @Path("{id}")
    @PUT
    @Operation(summary = "Update attribute value", operationId = "updateAttributeValue")
    @Valid WsAttributeValueRef updateAttributeValue(
            @Parameter(name = "id", description = "The attribute value id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The attribute value to update")
            @Valid WsAttributeValue wsAttributeValue);

    @Path("{id}")
    @GET
    @Operation(summary = "Get attribute value", operationId = "getAttributeValue")
    @Valid WsAttributeValue getAttributeValue(
            @Parameter(name = "id", description = "The attribute value id", required = true)
            @PathParam("id") long id);

}
