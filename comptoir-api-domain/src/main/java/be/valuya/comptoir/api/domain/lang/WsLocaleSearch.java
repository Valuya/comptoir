package be.valuya.comptoir.api.domain.lang;

import java.io.Serializable;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author cghislai
 */
@XmlRootElement(name = "LocaleText")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsLocaleSearch implements Serializable {

    @NotNull
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
