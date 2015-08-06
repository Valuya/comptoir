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
public class ToWsLocaleTextConverter {

    public WsLocaleText convert(LocaleText localeText) {
        Long id = localeText.getId();
        Map<Locale, String> localeTextMap = localeText.getLocaleTextMap();

        WsLocaleText wsLocaleText = new WsLocaleText();
        wsLocaleText.setId(id);
        wsLocaleText.setLocaleTextMap(localeTextMap);

        return wsLocaleText;
    }

}
