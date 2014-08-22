package be.valuya.comptoir.web.control;

import be.valuya.comptoir.model.commercial.Item;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ItemUtil {

    public static List<String> getItemListReferences(List<Item> items) {
        List<String> references = items.stream().map(Item::getReference).collect(Collectors.toList());
        return references;
    }

}
