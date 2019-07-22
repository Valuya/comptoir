package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPricing;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class FromWsPricingConverter {

    public Pricing fromWsPricing(WsPricing wsPricing) {
        switch (wsPricing) {

            case ABSOLUTE:
                return Pricing.ABSOLUTE;
            case ADD_TO_BASE:
                return Pricing.ADD_TO_BASE;
            case PARENT_ITEM:
                return Pricing.PARENT_ITEM;
            default:
                throw new IllegalArgumentException(Objects.toString(wsPricing));
        }
    }
}
