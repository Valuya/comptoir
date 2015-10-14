package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.search.WsLocaleSearch;
import be.valuya.comptoir.model.search.LocaleSearch;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class FromWsLocaleSearchConverter {

    public LocaleSearch convert(WsLocaleSearch wsLocaleSearch) {
        if (wsLocaleSearch == null) {
            return null;
        }
        LocaleSearch localeSearch = new LocaleSearch();

        Locale locale = wsLocaleSearch.getLocale();
        localeSearch.setLocale(locale);

        return localeSearch;
    }
}
