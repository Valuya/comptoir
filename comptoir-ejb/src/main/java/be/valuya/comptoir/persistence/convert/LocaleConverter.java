package be.valuya.comptoir.persistence.convert;

import java.util.Locale;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Converter(autoApply = true)
public class LocaleConverter implements AttributeConverter<String, Locale> {

    @Override
    public Locale convertToDatabaseColumn(String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }

    @Override
    public String convertToEntityAttribute(Locale locale) {
        return locale.toLanguageTag();
    }

}
