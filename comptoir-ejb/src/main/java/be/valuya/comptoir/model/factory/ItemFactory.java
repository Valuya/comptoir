package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.commercial.ItemVariant;
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

    public ItemVariant createItem(@Nonnull Company company) {
        LocaleText descriptionLocaleText = localeTextFactory.createLocaleText();
        LocaleText nameLocaleText = localeTextFactory.createLocaleText();
        Price price = priceFactory.createPrice(company);

        ItemVariant itemVariant = new ItemVariant();
        itemVariant.setCompany(company);
        itemVariant.setDescription(descriptionLocaleText);
        itemVariant.setName(nameLocaleText);
        itemVariant.setCurrentPrice(price);

        return itemVariant;
    }

}
