package ua.griddynamics.httpserver.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-14
 */
public class RequestUtil {
    private static final Set<String> HTTP_METHODS = new HashSet<>();

    static {
        HTTP_METHODS.add("GET");
        HTTP_METHODS.add("POST");
        HTTP_METHODS.add("PUT");
        HTTP_METHODS.add("UPDATE");
        HTTP_METHODS.add("HEAD");
        HTTP_METHODS.add("DELETE");
        HTTP_METHODS.add("PATCH");
        HTTP_METHODS.add("TRACE");
        HTTP_METHODS.add("CONNECT");
        HTTP_METHODS.add("OPTIONS");
    }

    public static boolean isMethodExist(String method) {
        return HTTP_METHODS.contains(method.toUpperCase());
    }
}
