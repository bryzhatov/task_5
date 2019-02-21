package ua.griddynamics.geekshop.res.templates.ftl.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class FreeMarkers {

    public static Map<String, Object> referByName(String name, Object value){
        Map<String, Object> map = new HashMap<>();
        map.put(name, value);
        return map;
    }

    public static Map<String, Object> referByClassName(Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(value.getClass().getSimpleName().toLowerCase(), value);
        return map;
    }
}
