package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.misc.LocaleText;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Singleton;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Singleton
public class LocaleTextFactory {

    public LocaleText createLocaleText() {
        LocaleText localeText = new LocaleText();
        Map<Locale, String> localeTextMap = new HashMap<>();
        localeText.setLocaleTextMap(localeTextMap);
        return localeText;
    }

}
