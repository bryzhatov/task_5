package ua.griddynamics.httpserver.session.api;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
@Data
public class Session {
    private Map<String, Object> values = new ConcurrentHashMap<>();

    public Object get(String key) {
        return values.get(key);
    }

    public void add(String key, Object value) {
        values.put(key, value);
    }

    public void remove(String key) {
        values.remove(key);
    }
}
