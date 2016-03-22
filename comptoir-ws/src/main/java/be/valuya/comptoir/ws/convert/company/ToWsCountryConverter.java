package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCountry;
import be.valuya.comptoir.api.domain.company.WsCountryRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.company.Country;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsCountryConverter {

    @Inject
    private ToWsLocaleTextConverter toWsLocaleTextConverter;

    public WsCountry convert(Country country) {
        if (country == null) {
            return null;
        }
        String code = country.getCode();
        BigDecimal defaultVatRate = country.getDefaultVatRate();

        WsCountry wsCountry = new WsCountry();
        wsCountry.setCode(code);
        wsCountry.setDefaultVatRate(defaultVatRate);
        return wsCountry;
    }

    public WsCountryRef reference(Country country) {
        String code = country.getCode();
        WsCountryRef wsCountryRef = new WsCountryRef(code);
        return wsCountryRef;
    }

}
