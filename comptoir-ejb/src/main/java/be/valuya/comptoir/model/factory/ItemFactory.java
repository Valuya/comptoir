package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.misc.LocaleText;
import javax.annotation.Nonnull;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemFactory {

    public static Item createItem(@Nonnull Company company) {
        LocaleText descriptionLocaleText = new LocaleText();
        LocaleText nameLocaleText = new LocaleText();

        Item item = new Item();
        item.setCompany(company);
        item.setDescription(descriptionLocaleText);
        item.setName(nameLocaleText);

        return item;
    }

}
