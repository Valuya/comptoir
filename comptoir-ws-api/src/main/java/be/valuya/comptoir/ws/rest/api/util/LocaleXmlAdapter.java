/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.rest.api.util;

import java.util.Locale;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author cghislai
 */
public class LocaleXmlAdapter extends XmlAdapter<String, Locale> {

    @Override
    public Locale unmarshal(String v) throws Exception {
        return Locale.forLanguageTag(v);
    }

    @Override
    public String marshal(Locale v) throws Exception {
        return v.getLanguage();
    }

}
