package ua.griddynamics.httpserver.api;

import java.io.StringWriter;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-13
 */
public interface HttpResponse {

    StringWriter getWriter();

    void addHeader(String key, String value);

    void addHeaderIfAbsent(String key, String value);

    String getHeader(String key);
}
