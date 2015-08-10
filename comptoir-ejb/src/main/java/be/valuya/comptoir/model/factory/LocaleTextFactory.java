package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.lang.LocaleText;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class LocaleTextFactory {

    public LocaleText createLocaleText() {
        LocaleText localeText = new LocaleText();
        Map<Locale, String> localeTextMap = new HashMap<>();
        localeText.setLocaleTextMap(localeTextMap);
        return localeText;
    }

}
