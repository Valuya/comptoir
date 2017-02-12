package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCountry;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.service.CountryService;
import be.valuya.comptoir.ws.convert.company.ToWsCountryConverter;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/country")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({Roles.EMPLOYEE})
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

    @Path("/search")
    @POST
    public List<WsCountry> findContries() {
        List<Country> allCountries = countryService.findAllCountries();
        List<WsCountry> wsCountries = allCountries.stream()
                .map(toWsCountryConverter::convert)
                .collect(Collectors.toList());
        return wsCountries;
    }

}
