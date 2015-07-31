package be.valuya.comptoir.web.convert;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.service.AccountService;
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
public class AccountConverter implements Converter {

    @Inject
    private transient AccountService accountService;

    @Override
    public Account getAsObject(FacesContext context, UIComponent component, String valueStr) {
        if (valueStr == null || valueStr.trim().isEmpty()) {
            return null;
        }
        Long id = Long.valueOf(valueStr);
        return accountService.findAccountById(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        Account account = (Account) value;
        Long id = account.getId();
        String idStr = Long.toString(id);
        return idStr;
    }
}
