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
public class LocalDateTimeConverter implements AttributeConverter<Date, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.from(instant);
    }

    @Override
    public Date convertToEntityAttribute(LocalDateTime localDateTime) {
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }
}
