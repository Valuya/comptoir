package be.valuya.comptoir.ws.api;

import be.valuya.comptoir.api.domain.company.WsCountry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CountryResourceApi {

    @Path("{code}")
    @Valid
    @GET
    @Operation(summary = "Get a country", operationId = "getCountry")
    WsCountry getCountry(
            @Parameter(name = "code", required = true, description = "The country code")
            @PathParam("code") String code
    );

    @Path("/search")
    @POST
    @Operation(summary = "Search countries", operationId = "searchCountries")
    List<WsCountry> findCountries();


}
