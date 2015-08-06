package be.valuya.comptoir.comptoir.ws.convert.text;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.lang.LocaleText;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsLocaleTextConverter {

    public LocaleText convert(WsLocaleText wsLocaleText) {
        Long id = wsLocaleText.getId();
        Map<Locale, String> localeTextMap = wsLocaleText.getLocaleTextMap();

        LocaleText localeText = new LocaleText();
        localeText.setId(id);
        localeText.setLocaleTextMap(localeTextMap);

        return localeText;
    }

}
