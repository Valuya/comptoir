package be.valuya.comptoir.web.convert;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros
 */
@Named
@RequestScoped
public class ZonedDateTimeConverter implements Converter {

    private DateTimeFormatter dateTimeFormatter;

    @PostConstruct
    public void init() {
        Locale userLocale = Locale.FRENCH;
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", userLocale);
    }

    @Override
    public ZonedDateTime getAsObject(FacesContext context, UIComponent component, String valueStr) {
        try {
            if (valueStr == null) {
                return null;
            }
            LocalDateTime localDateTime = LocalDateTime.parse(valueStr, dateTimeFormatter);
            TimeZone userTimeZone = TimeZone.getDefault();
            ZoneId zoneId = userTimeZone.toZoneId();
            return ZonedDateTime.of(localDateTime, zoneId);
        } catch (DateTimeException dateTimeException) {
            throw new ConverterException("Erreur de conversion : " + valueStr, dateTimeException);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = (ZonedDateTime) value;
        return dateTimeFormatter.format(zonedDateTime);
    }
}
