package ua.griddynamics.httpserver.api;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-13
 */
public interface HttpResponse {
    void write(String response);

    void write(byte[] response);

    void addHeader(String key, String value);

    void addHeaderIfAbsent(String key, String value);

    String getHeader(String key);

    void setStatus(int status);
}
