package ua.griddynamics.httpserver.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
public class HttpCodes {
    private final Map<Integer, String> config = new HashMap<>();

    public HttpCodes() {
        initDefault();
    }

    public String getMessage(int code) {
        return config.get(code);
    }

    private void initDefault() {
        config.put(200, "OK");
        config.put(403, "Forbidden");
        config.put(404, "Not Found");
        config.put(405, "Method Not Allowed");
        config.put(501, "Not Implemented");
        config.put(505, "HTTP Version Not Supported");
    }
}
