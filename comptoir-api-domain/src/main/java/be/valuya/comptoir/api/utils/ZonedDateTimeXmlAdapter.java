package be.valuya.comptoir.api.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author cghislai
 */
public class ZonedDateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime>{

    @Override
    public ZonedDateTime unmarshal(String string) throws Exception {
        return ZonedDateTime.parse(string, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public String marshal(ZonedDateTime dateTime) throws Exception {
        return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    
}
