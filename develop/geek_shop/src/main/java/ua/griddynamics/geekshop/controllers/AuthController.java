package ua.griddynamics.geekshop.controllers;

import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class AuthController {
    private final SessionManager sessionManager;

    public AuthController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void auth(HttpRequest request, HttpResponse response) {
        String[] mass = new String(request.getBody()).split("[=&]");
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < mass.length - 1; i++) {
            map.put(mass[i], mass[i + 1]);
        }

        String login = map.get("login");
        String password = map.get("password");

        Session session = new Session();
        sessionManager.add("12312", session);
        session.add("user", new User(1, "Dima", "Bryzhatov"));
        response.addHeader("Set-Cookie", "sessionId=12312");
        response.write("Ok");
    }
}
