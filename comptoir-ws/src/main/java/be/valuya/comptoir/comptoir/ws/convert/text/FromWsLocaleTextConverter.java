package be.valuya.comptoir.comptoir.ws.convert.text;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.lang.LocaleText;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsLocaleTextConverter {

    public LocaleText convert(List<WsLocaleText> wsLocaleTexts) {
        Map<Locale, String> localeTextMap = wsLocaleTexts.stream()
                .collect(Collectors.toMap(WsLocaleText::getLocale, WsLocaleText::getText));

        LocaleText localeText = new LocaleText();
        localeText.setLocaleTextMap(localeTextMap);

        return localeText;
    }

}
