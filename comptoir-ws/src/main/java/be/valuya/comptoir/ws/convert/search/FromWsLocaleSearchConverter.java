/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.convert.search;

import be.valuya.comptoir.api.domain.lang.WsLocaleSearch;
import be.valuya.comptoir.model.search.LocaleSearch;
import java.util.Locale;

/**
 *
 * @author cghislai
 */
public class FromWsLocaleSearchConverter {
    
    public LocaleSearch convert(WsLocaleSearch wsLocaleSearch) {
        LocaleSearch localeSearch = new LocaleSearch();
        
        Locale locale = wsLocaleSearch.getLocale();
        localeSearch.setLocale(locale);
        
        return localeSearch;
    }
}
