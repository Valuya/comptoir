package be.valuya.comptoir.persistence.convert;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<Date, ZonedDateTime> {

    @Override
    public ZonedDateTime convertToDatabaseColumn(Date date) {
        Instant instant = date.toInstant();
        return ZonedDateTime.from(instant);
    }

    @Override
    public Date convertToEntityAttribute(ZonedDateTime zonedDateTime) {
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }
}
