package be.valuya.comptoir.web.convert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.annotation.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Yannick Majoros
 */
@FacesConverter("VatRateConverter")
@ManagedBean
public class VatRateConverter implements Converter {

    public static final BigDecimal PERCENTS_IN_UNIT = BigDecimal.valueOf(100L);
    //
    private final DecimalFormat decimalFormat;

    public VatRateConverter() {
        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
    }

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String valueStr) {
        try {
            String numberStr = valueStr.replaceAll("(.*)%(.*)", "$1$2");
            if (numberStr == null || numberStr.trim().isEmpty()) {
                return null;
            }
            BigDecimal value = (BigDecimal) decimalFormat.parse(numberStr);
            if (value == null) {
                return null;
            }
            return value.divide(PERCENTS_IN_UNIT);
        } catch (ParseException parseException) {
            throw new NumberFormatException("Erreur de conversion : " + valueStr);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        BigDecimal bdValue = (BigDecimal) value;
        if (bdValue == null) {
            return null;
        }
        BigDecimal percentsValue = bdValue.multiply(PERCENTS_IN_UNIT);
        String valueStr = decimalFormat.format(percentsValue) + " %";
        return valueStr;
    }
}
