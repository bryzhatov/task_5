package ua.griddynamics.httpserver.entity;

import lombok.Data;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.io.StringWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
@Data
public class Response implements HttpResponse {
    private final Map<String, String> headers = new HashMap<>();
    private final StringWriter writer = new StringWriter();
    private final Socket socket;
    private int status;

    public Response(Request request) {
        this.socket = request.getConnection();
    }

    @Override
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public void addHeaderIfAbsent(String key, String value) {
        headers.putIfAbsent(key, value);
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }
}
