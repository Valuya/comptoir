package be.valuya.comptoir.model.search;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cghislai
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocaleSearch {

    @NotNull
    @Nonnull
    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
