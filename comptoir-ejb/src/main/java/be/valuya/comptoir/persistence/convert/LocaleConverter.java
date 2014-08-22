package be.valuya.comptoir.persistence.convert;

import java.util.Locale;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Converter(autoApply = true)
public class LocaleConverter implements AttributeConverter<Locale, String> {

    @Override
    public String convertToDatabaseColumn(Locale locale) {
        if (locale == null) {
            return null;
        }
        return locale.toLanguageTag();
    }

    @Override
    public Locale convertToEntityAttribute(String languageTag) {
        if (languageTag == null) {
            return null;
        }
        return Locale.forLanguageTag(languageTag);

    }

}
