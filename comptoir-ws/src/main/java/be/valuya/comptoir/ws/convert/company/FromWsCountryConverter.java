package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCountry;
import be.valuya.comptoir.api.domain.company.WsCountryRef;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.service.CountryService;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsCountryConverter {

    @EJB
    private CountryService countryService;

    public Country convert(WsCountry wsCountry) {
        if (wsCountry == null) {
            return null;
        }
        Country country = new Country();

        return patch(country, wsCountry);
    }

    public Country patch(Country country, WsCountry wsCountry) {
        String code = wsCountry.getCode();
        BigDecimal defaultVatRate = wsCountry.getDefaultVatRate();

        country.setCode(code);
        country.setDefaultVatRate(defaultVatRate);
        return country;
    }

    public Country find(WsCountryRef wsCountryRef) {
        if (wsCountryRef == null) {
            return null;
        }
        String code = wsCountryRef.getCode();
        return countryService.getCountryByCode(code);
    }

}
