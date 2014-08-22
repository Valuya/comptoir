package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Price;
import java.math.BigDecimal;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class VatUtils {

    public static BigDecimal calcVatAmount(Price price) {
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal vatRate = price.getVatRate();
        BigDecimal vatAmount = calcVatAmount(vatExclusive, vatRate);
        return vatAmount;
    }

    public static BigDecimal calcVatAmount(BigDecimal vatExclusive, BigDecimal vatRate) {
        BigDecimal vatAmount = vatExclusive.multiply(vatRate);
        return vatAmount;
    }

    public static BigDecimal calcVatInclusive(Price price) {
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal vatRate = price.getVatRate();
        return calcVatInclusive(vatExclusive, vatRate);
    }

    public static BigDecimal calcVatInclusive(BigDecimal vatExclusive, BigDecimal vatRate) {
        BigDecimal vatAmount = calcVatAmount(vatExclusive, vatRate);
        BigDecimal vatInclusive = vatExclusive.add(vatAmount);
        return vatInclusive;
    }

}
