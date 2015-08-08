package be.valuya.comptoir.api.domain.lang;

import java.io.Serializable;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "locale_text")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsLocaleText implements Serializable {

    @NotNull
    private Locale locale;
    @NotNull
    private String text;

    public WsLocaleText() {
    }

    public WsLocaleText(Locale locale, String text) {
        this.locale = locale;
        this.text = text;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
