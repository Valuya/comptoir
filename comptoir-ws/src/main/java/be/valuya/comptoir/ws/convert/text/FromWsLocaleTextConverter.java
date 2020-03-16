package be.valuya.comptoir.ws.convert.text;

import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsLocaleTextConverter {

    public LocaleText convert(List<WsLocaleText> wsLocaleTexts) {
        LocaleText newTexts = new LocaleText();
        List<WsLocaleText> sourcesTexts = Optional.ofNullable(wsLocaleTexts)
                .orElseGet(ArrayList::new);
        return update(sourcesTexts, newTexts);
    }

    public LocaleText update(List<WsLocaleText> texts, LocaleText existingLocaleText) {
        List<WsLocaleText> sourcesTexts = Optional.ofNullable(texts)
                .orElseGet(ArrayList::new);

        Map<Locale, String> localeTextMap = sourcesTexts.stream()
                .filter(t -> t.getText() != null)
                .collect(Collectors.toMap(
                        WsLocaleText::getLocale,
                        WsLocaleText::getText,
                        throwingMerger())
                );

        if (existingLocaleText == null) {
            existingLocaleText = new LocaleText();
        }
        existingLocaleText.setLocaleTextMap(localeTextMap);

        return existingLocaleText;
    }

    private static <LocaleText> BinaryOperator<LocaleText> throwingMerger() {
        return (locale, v) -> {
            throw new BadRequestException(String.format("Duplicate key for locale %s", locale));
        };
    }

}
