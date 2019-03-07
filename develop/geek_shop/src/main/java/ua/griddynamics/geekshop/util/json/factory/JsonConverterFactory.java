package ua.griddynamics.geekshop.util.json.factory;

import ua.griddynamics.geekshop.util.json.converter.GsonConverter;
import ua.griddynamics.geekshop.util.json.converter.JsonConverter;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-07
 */
public class JsonConverterFactory {
    public static JsonConverter create(String name) {
        switch (name) {
            case "gson":
            default:
                return new GsonConverter();
        }
    }
}
