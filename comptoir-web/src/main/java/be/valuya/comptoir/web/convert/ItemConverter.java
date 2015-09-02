package be.valuya.comptoir.web.convert;

import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.service.StockService;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros
 */
@Named
@RequestScoped
public class ItemConverter implements Converter {

    @Inject
    private StockService stockService;

    @Override
    public ItemVariant getAsObject(FacesContext context, UIComponent component, String valueStr) {
        if (valueStr == null || valueStr.trim().isEmpty()) {
            return null;
        }
        Long id = Long.valueOf(valueStr);
        return stockService.findItemVariantById(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        ItemVariant itemVariant = (ItemVariant) value;
        Long id = itemVariant.getId();
        String idStr = Long.toString(id);
        return idStr;
    }
}
