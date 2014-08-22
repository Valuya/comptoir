package be.valuya.comptoir.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class TextService {

    public List<Locale> findAllLocales() {
        return Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
    }

}
