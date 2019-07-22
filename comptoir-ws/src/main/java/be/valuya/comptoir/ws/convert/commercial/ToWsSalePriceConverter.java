package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalePrice;
import be.valuya.comptoir.model.commercial.SalePrice;

import java.math.BigDecimal;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsSalePriceConverter {

    public WsSalePrice convert(SalePrice salePrice) {
        if (salePrice == null) {
            return null;
        }
        BigDecimal base = salePrice.getBase();
        BigDecimal taxes = salePrice.getTaxes();

        WsSalePrice wsSalePrice = new WsSalePrice();
        wsSalePrice.setBase(base);
        wsSalePrice.setTaxes(taxes);
        return wsSalePrice;
    }

}
