package be.valuya.web.control;

import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.web.view.Views;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class ItemListController implements Serializable {

    @Inject
    private LazyDataModel<Item> itemDataModel;
    private ItemSearch itemSearch;

    public String actionShow() {
        return Views.ITEM_LIST;
    }

    public void actionSearch() {
    }

    public LazyDataModel<Item> getItemDataModel() {
        return itemDataModel;
    }

    public ItemSearch getItemSearch() {
        return itemSearch;
    }

}
