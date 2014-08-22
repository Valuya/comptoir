package be.valuya.comptoir.web.convert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
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
public class EuroConverter implements Converter {

    private final DecimalFormat decimalFormat;

    public EuroConverter() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.FRENCH);
        decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
    }

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String valueStr) {
        try {
            if (valueStr == null) {
                return null;
            }
            String numberStr = valueStr.replaceAll("(.*)€(.*)", "$1$2").replace(".", ",");
            if (numberStr == null || numberStr.trim().isEmpty()) {
                return null;
            }
            BigDecimal valueNumber = (BigDecimal) decimalFormat.parse(numberStr);
            return valueNumber;
        } catch (ParseException parseException) {
            throw new ConverterException("Erreur de conversion : " + valueStr, parseException);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        String euroStr = decimalFormat.format(value) + " €";
        return euroStr;
    }
}
