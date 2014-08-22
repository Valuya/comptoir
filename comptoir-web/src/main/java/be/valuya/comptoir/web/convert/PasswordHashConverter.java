package be.valuya.comptoir.web.convert;

import javax.annotation.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Yannick Majoros
 */
@FacesConverter("PasswordHashConverter")
@ManagedBean
public class PasswordHashConverter implements Converter {

    @Override
    public String getAsObject(FacesContext context, UIComponent component, String valueStr) {
        if (valueStr == null) {
            return null;
        }
        return valueStr;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return null;
    }
}
