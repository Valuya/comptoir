package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCountry;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.service.CountryService;
import be.valuya.comptoir.ws.convert.company.ToWsCountryConverter;

import java.math.BigDecimal;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/country")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CountryResource {

    @Inject
    private ToWsCountryConverter toWsCountryConverter;
    @Inject
    private CountryService countryService;

    @Path("{code}")
    @Valid
    @GET
    public WsCountry getCountry(@NotNull @PathParam("code") String code) {
        Country country = countryService.getCountryByCode(code);
        WsCountry wsCountry = toWsCountryConverter.convert(country);
        return wsCountry;
    }

}
