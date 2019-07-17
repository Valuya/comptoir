package be.valuya.comptoir.ws.rest.api.domain.lang;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Locale;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "LocaleText")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(name = "WsLocaleText", description = "Localized message")
public class WsLocaleText implements Serializable {

    @NotNull
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    @Schema(type = SchemaType.STRING, required = true)
    private Locale locale;
    @NotNull
    @Schema(type = SchemaType.STRING, required = true)
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
