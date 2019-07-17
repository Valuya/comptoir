package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCountry;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.service.CountryService;
import be.valuya.comptoir.ws.rest.api.CountryResourceApi;
import be.valuya.comptoir.ws.convert.company.ToWsCountryConverter;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCountryRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCountrySearchResult;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;

    public WsCountry getCountry(String code) {
        Country country = countryService.getCountryByCode(code);
        WsCountry wsCountry = toWsCountryConverter.convert(country);
        return wsCountry;
    }

    public WsCountrySearchResult findCountries() {
        Pagination<Country, ?> pagination = restPaginationUtil.extractPagination(uriInfo);
        List<Country> allCountries = countryService.findAllCountries();
        List<WsCountryRef> wsCountries = allCountries.stream()
                .map(toWsCountryConverter::reference)
                .collect(Collectors.toList());
        return restPaginationUtil.setResults(new WsCountrySearchResult(), wsCountries, pagination);
    }

}
