package be.valuya.comptoir.ws.convert.text;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.lang.LocaleText;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsLocaleTextConverter {

    public List<WsLocaleText> convert(LocaleText localeText) {
        if (localeText == null) {
            return null;
        }
        List<WsLocaleText> localeTextList = localeText.getLocaleTextMap()
                .entrySet()
                .stream()
                .map(entry -> new WsLocaleText(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        
        return localeTextList;
    }

}
