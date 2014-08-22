package be.valuya.comptoir.persistence.convert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public LocalDateTime convertToEntityAttribute(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        return LocalDateTime.from(instant);
    }

    @Override
    public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }
}
