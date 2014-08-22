package be.valuya.comptoir.persistence.convert;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {

    @Override
    public ZonedDateTime convertToEntityAttribute(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        return ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }

    @Override
    public Date convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }
}
