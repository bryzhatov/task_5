package ua.griddynamics.geekshop;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.httpserver.session.api.Meta;
import ua.griddynamics.httpserver.session.api.Session;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-15
 */
public class Test {
    private static final Session session = new Session();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        session.add("user", new User(1, "dima", "bryzhatov", "2131"));

        Gson gson = new Gson();
        String sessionJson = gson.toJson(session);
        System.out.println(sessionJson);


        Session se = gson.fromJson(sessionJson, Session.class);

        for (Map.Entry<String, Meta> entry : se.getValues().entrySet()) {
            String jsonObject = entry.getValue().getObject().toString().replace("=", ":");
            Object object = gson.fromJson(jsonObject, Class.forName(entry.getValue().getClassName()));
            entry.getValue().setObject(object);
        }
    }
}
