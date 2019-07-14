package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCountry;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.service.CountryService;
import be.valuya.comptoir.ws.api.CountryResourceApi;
import be.valuya.comptoir.ws.convert.company.ToWsCountryConverter;

import javax.annotation.security.PermitAll;
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
@PermitAll
public class CountryResource implements CountryResourceApi {

    @Inject
    private ToWsCountryConverter toWsCountryConverter;
    @Inject
    private CountryService countryService;

    public WsCountry getCountry(String code) {
        Country country = countryService.getCountryByCode(code);
        WsCountry wsCountry = toWsCountryConverter.convert(country);
        return wsCountry;
    }

    public List<WsCountry> findCountries() {
        List<Country> allCountries = countryService.findAllCountries();
        List<WsCountry> wsCountries = allCountries.stream()
                .map(toWsCountryConverter::convert)
                .collect(Collectors.toList());
        return wsCountries;
    }

}
