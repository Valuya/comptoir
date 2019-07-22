package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.model.commercial.Pricing;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPricing;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class ToWsPricingConverter {

    public WsPricing toWsPricing(Pricing pricing) {
        switch (pricing) {

            case ABSOLUTE:
                return WsPricing.ABSOLUTE;
            case ADD_TO_BASE:
                return WsPricing.ADD_TO_BASE;
            case PARENT_ITEM:
                return WsPricing.PARENT_ITEM;
            default:
                throw new IllegalArgumentException(Objects.toString(pricing));
        }
    }
}
