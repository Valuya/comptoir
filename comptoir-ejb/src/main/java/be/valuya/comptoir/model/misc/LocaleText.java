package be.valuya.comptoir.model.misc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "locale_text")
public class LocaleText implements Serializable {

    @Id
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "locale", insertable = false, updatable = false)
    @Column(name = "value")
    @CollectionTable(name = "locale_text", joinColumns = @JoinColumn(name = "locale_text_id", columnDefinition = "TEXT"))
    private Map<Locale, String> localeTextMap = new HashMap<>();

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

}
