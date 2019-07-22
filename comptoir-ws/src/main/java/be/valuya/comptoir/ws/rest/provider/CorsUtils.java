package be.valuya.comptoir.ws.rest.provider;

import be.valuya.comptoir.util.HeaderUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorsUtils {

    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String HEADER_ORIGIN = "origin";
    private static final String HEADER_REQUESTED_METHOD = "Access-Control-Request-Method";
    private static final String HEADER_REQUESTED_HEADERS = "Access-Control-Request-Headers";

    public static boolean isCorsPreflightRequest(String method, MultivaluedMap<String, String> headers) {
        return method.equals(METHOD_OPTIONS) && headers.containsKey(HEADER_REQUESTED_METHOD);
    }

    public static boolean isAllowedOrigin(MultivaluedMap<String, String> headers, Collection<String> wsCorsAllowedOrigins) {
        return getRequestOrigin(headers)
                .map(origin -> CorsUtils.isAllowedOrigin(origin, wsCorsAllowedOrigins))
                .orElse(false);
    }

    public static boolean isRequestedMethodAllowed(MultivaluedMap<String, String> headers, Set<String> allowedMethods) {
        return HeaderUtils.getFirstHeaderIgnoreCase(HEADER_REQUESTED_METHOD, headers)
                .map(method -> CorsUtils.isMethodAllowed(method, allowedMethods))
                .orElse(false);
    }

    public static boolean isRequestedHeadersAllowed(MultivaluedMap<String, String> headers, Set<String> allowedHeaders) {
        return HeaderUtils.getHeadersIgnoreCase(HEADER_REQUESTED_HEADERS, headers)
                .map(requestedHeaders -> CorsUtils.areRequestedHeadersAllowed(requestedHeaders, allowedHeaders))
                .orElse(false);
    }

    private static boolean isMethodAllowed(String method, Set<String> allowedMethods) {
        return allowedMethods.stream()
                .anyMatch(method::equalsIgnoreCase);
    }


    public static Optional<String> getRequestOrigin(MultivaluedMap<String, String> headers) {
        return HeaderUtils.getFirstHeaderIgnoreCase(HEADER_ORIGIN, headers);
    }

    private static boolean isAllowedOrigin(String origin, Collection<String> wsCorsAllowedOrigins) {
        return wsCorsAllowedOrigins.stream()
                .map(whiteListItem -> CorsUtils.doesWhiteListItemMatchOrigin(origin, whiteListItem))
                .findAny()
                .orElse(false);
    }

    private static boolean doesWhiteListItemMatchOrigin(String origin, String whiteListItem) {
        if (whiteListItem.equals("*")) {
            return true;
        }
        Pattern pattern = Pattern.compile(whiteListItem);
        Matcher matcher = pattern.matcher(origin);
        return matcher.matches();
    }

    private static boolean areRequestedHeadersAllowed(List<String> requestedHeaders, Set<String> allowedHeaders) {
        return requestedHeaders.stream()
                .flatMap(headerValue -> Arrays.stream(headerValue.split(",")))
                .allMatch(h -> CorsUtils.isRequestedHeaderAllowed(h, allowedHeaders));
    }

    private static boolean isRequestedHeaderAllowed(String header, Set<String> allowedHeaders) {
        return allowedHeaders.stream()
                .anyMatch(header::equalsIgnoreCase);
    }


}
