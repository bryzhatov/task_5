package ua.griddynamics.httpserver.api;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-13
 */
public interface HttpResponse {

    void write(Reader stream) throws IOException;

    void write(String string) throws IOException;

    void addHeader(String key, String value);

    void addHeaderIfAbsent(String key, String value);

    String getHeader(String key);

    void setStatus(int status);

    StringWriter getWriter();
}
