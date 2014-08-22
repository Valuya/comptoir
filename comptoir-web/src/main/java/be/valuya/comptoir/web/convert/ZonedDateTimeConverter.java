package be.valuya.comptoir.web.convert;

import be.valuya.comptoir.web.control.LoginController;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros
 */
@Named
@RequestScoped
public class ZonedDateTimeConverter implements Converter {

    @Inject
    private transient LoginController loginController;
    //
    private DateTimeFormatter dateTimeFormatter;

    @PostConstruct
    public void init() {
        Locale userLocale = loginController.getUserLocale();
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", userLocale);
    }

    @Override
    public ZonedDateTime getAsObject(FacesContext context, UIComponent component, String valueStr) {
        try {
            if (valueStr == null) {
                return null;
            }
            TemporalAccessor temporalAccessor = dateTimeFormatter.parse(valueStr);
            return ZonedDateTime.from(temporalAccessor);
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
