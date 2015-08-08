package be.valuya.comptoir.ws.convert.text;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.lang.LocaleText;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsLocaleTextConverter {

    public LocaleText convert(List<WsLocaleText> wsLocaleTexts) {
        LocaleText localeText = new LocaleText();

        return update(wsLocaleTexts, localeText);
    }

    public LocaleText update(List<WsLocaleText> wsLocaleTexts, LocaleText existingLocaleText) {
        Map<Locale, String> localeTextMap = wsLocaleTexts.stream()
                .collect(Collectors.toMap(WsLocaleText::getLocale, WsLocaleText::getText));

        if (existingLocaleText != null) {
            existingLocaleText.setLocaleTextMap(localeTextMap);
        }

        return existingLocaleText;
    }

    private static <LocaleText> BinaryOperator<LocaleText> throwingMerger() {
        return (locale, v) -> {
            throw new BadRequestException(String.format("Duplicate key for locale %s", locale));
        };
    }

}
