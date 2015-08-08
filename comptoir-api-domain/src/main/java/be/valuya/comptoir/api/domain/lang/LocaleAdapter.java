package be.valuya.comptoir.api.domain.lang;

import java.util.Locale;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class LocaleAdapter extends XmlAdapter<String, Locale> {

    @Override
    public Locale unmarshal(String language) throws Exception {
        return new Locale(language);
    }

    @Override
    public String marshal(Locale locale) throws Exception {
        return locale.getLanguage();
    }

}
