package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class ItemFactory {

    @Inject
    private LocaleTextFactory localeTextFactory;
    @Inject
    private PriceFactory priceFactory;

    public Item createItem(@Nonnull Company company) {
        LocaleText descriptionLocaleText = localeTextFactory.createLocaleText();
        LocaleText nameLocaleText = localeTextFactory.createLocaleText();
        Price price = priceFactory.createPrice(company);

        Item item = new Item();
        item.setCompany(company);
        item.setDescription(descriptionLocaleText);
        item.setName(nameLocaleText);
        item.setCurrentPrice(price);

        return item;
    }

}
