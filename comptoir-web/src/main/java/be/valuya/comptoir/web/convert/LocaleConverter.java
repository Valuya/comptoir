package be.valuya.comptoir.web.convert;

import java.util.Locale;
import javax.annotation.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Yannick Majoros
 */
@FacesConverter("LocaleConverter")
@ManagedBean
public class LocaleConverter implements Converter {

    @Override
    public Locale getAsObject(FacesContext context, UIComponent component, String valueStr) {
        if (valueStr == null) {
            return null;
        }
        return Locale.forLanguageTag(valueStr);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        Locale locale = (Locale) value;
        return locale.toLanguageTag();
    }
}
