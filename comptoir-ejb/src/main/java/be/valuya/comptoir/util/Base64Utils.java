package be.valuya.comptoir.util;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

public class Base64Utils {

    public static String encodeBase64(String token) {
        try {
            String base64Str = DatatypeConverter.printBase64Binary(token.getBytes("utf-8"));
            return base64Str;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException(unsupportedEncodingException);
        }
    }

    public static String decodeBase64(String token) {
        try {
            byte[] base64bytes = DatatypeConverter.parseBase64Binary(token);
            String decoded = new String(base64bytes, 0, base64bytes.length, "utf-8");
            return decoded;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException(unsupportedEncodingException);
        }
    }

}
