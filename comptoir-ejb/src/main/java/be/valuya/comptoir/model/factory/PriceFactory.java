package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.company.Company;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Nonnull;
import javax.ejb.Stateless;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class PriceFactory {

    public Price createPrice(@Nonnull Company company) {
        BigDecimal vatRate = BigDecimal.valueOf(2100, 4);
        ZonedDateTime startDateTime = ZonedDateTime.now();

        Price price = new Price();
        price.setStartDateTime(startDateTime);
        price.setVatRate(vatRate);

        return price;
    }

}
