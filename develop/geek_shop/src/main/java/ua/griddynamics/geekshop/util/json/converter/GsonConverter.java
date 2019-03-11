package ua.griddynamics.geekshop.util.json.converter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-07
 */
public class GsonConverter implements JsonConverter {

    private final Gson gson;

    public GsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeHierarchyAdapter(Collection.class, new CollectionAdapter());
        gson = gsonBuilder.create();
    }

    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T fromJson(String json, Class<T> c) {
        return gson.fromJson(json, c);
    }

    private static class CollectionAdapter implements JsonSerializer<Collection<?>> {
        @Override
        public JsonElement serialize(Collection<?> src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null || src.isEmpty())
                return null;

            JsonArray array = new JsonArray();

            for (Object child : src) {
                JsonElement element = context.serialize(child);
                array.add(element);
            }

            return array;
        }
    }
}
