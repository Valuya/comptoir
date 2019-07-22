package be.valuya.comptoir.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HeaderUtils {

    public static Optional<String> getHeaderIgnoreCase(String headerName, HttpServletRequest servletRequest) {
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        return Collections.list(headerNames).stream()
                .filter(header -> header.equalsIgnoreCase(headerName))
                .findAny()
                .map(servletRequest::getHeader);
    }

    public static Optional<String> getFirstHeaderIgnoreCase(String headerName, MultivaluedMap<String, String> headers) {
        Set<String> headerNames = headers.keySet();
        return headerNames.stream()
                .filter(header -> header.equalsIgnoreCase(headerName))
                .findAny()
                .map(headers::getFirst);
    }


    public static Optional<List<String>> getHeadersIgnoreCase(String headerName, MultivaluedMap<String, String> headers) {
        Set<String> headerNames = headers.keySet();
        return headerNames.stream()
                .filter(header -> header.equalsIgnoreCase(headerName))
                .findAny()
                .map(headers::get);
    }
}
