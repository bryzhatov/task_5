package ua.griddynamics.geekshop.util.json.converter;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-07
 */
public interface JsonConverter {
    String toJson(Object object);

    <T> T fromJson(String json, Class<T> c);
}
