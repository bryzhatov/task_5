package ua.griddynamics.httpserver.entity;

import lombok.Data;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
@Data
public class Response implements HttpResponse {
    private final Map<String, String> headers = new HashMap<>();
    private final List<Byte> writer = new ArrayList<>();
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

    @Override
    public void write(String body) {
        for (byte i : body.getBytes()) {
            writer.add(i);
        }
    }

    @Override
    public void write(byte[] body) {
        for (byte i : body) {
            writer.add(i);
        }
    }

    public byte[] getBody() {
        byte[] body = new byte[writer.size()];
        for (int i = 0; i < writer.size(); i++) {
            body[i] = writer.get(i);
        }
        return body;
    }
}
