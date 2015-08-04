package be.valuya.comptoir.api.domain.lang;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
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

    private Long id;
    private Map<Locale, String> localeTextMap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Locale, String> getLocaleTextMap() {
        return localeTextMap;
    }

    public void setLocaleTextMap(Map<Locale, String> localeTextMap) {
        this.localeTextMap = localeTextMap;
    }

    public String get(Locale locale) {
        return localeTextMap.get(locale);
    }

    public String put(Locale locale, String text) {
        return localeTextMap.put(locale, text);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsLocaleText other = (WsLocaleText) obj;
        return Objects.equals(this.id, other.id);
    }

}
