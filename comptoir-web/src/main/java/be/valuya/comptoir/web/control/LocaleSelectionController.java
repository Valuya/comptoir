package be.valuya.comptoir.web.control;

import be.valuya.comptoir.service.TextService;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class LocaleSelectionController implements Serializable {

    @EJB
    private transient TextService textService;
    //
    private List<Locale> locales;

    @PostConstruct
    public void init() {
        locales = textService.findAllLocales();
    }

    public List<Locale> getLocales() {
        return locales;
    }

}
