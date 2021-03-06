package be.valuya.comptoir.ws.convert.text;

import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.lang.LocaleText;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsLocaleTextConverter {

    public List<WsLocaleText> convert(LocaleText localeText) {
        List<WsLocaleText> localeTextList = Optional.ofNullable(localeText)
                .map(LocaleText::getLocaleTextMap)
                .map(Map::entrySet)
                .orElseGet(HashSet::new)
                .stream()
                .map(entry -> new WsLocaleText(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return localeTextList;
    }

}
