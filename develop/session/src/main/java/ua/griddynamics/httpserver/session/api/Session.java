package ua.griddynamics.httpserver.session.api;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class Session implements Serializable {
    @Getter
    private Map<String, Object> values = new ConcurrentHashMap<>();

    public Object get(String key) {
        return key != null ? values.get(key).getObject() : null;
    }

    public void add(String key, Object value) {
        values.put(key, new Meta(value.getClass().getCanonicalName(), value));
    }

    public void remove(String key) {
        values.remove(key);
    }
}
