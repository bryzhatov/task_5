package ua.griddynamics.geekshop.controllers.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-14
 */
public class Model {
    private final Map<String, Object> modelMap = new HashMap<>();

    public void add(String key, Object value) {
        modelMap.put(key, value);
    }

    public Object get(String key) {
        return modelMap.get(key);
    }
}
