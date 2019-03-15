package ua.griddynamics.geekshop.controllers;

import ua.griddynamics.geekshop.controllers.entity.Model;
import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class AuthController {
    private final SessionService sessionService;

    public AuthController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public String auth(HttpRequest request, HttpResponse response, Model model) {
        String[] mass = new String(request.getBody()).split("[=&]");
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < mass.length - 1; i++) {
            map.put(mass[i], mass[i + 1]);
        }

        Session session = new Session();
        session.add("user", new User(1, "Dima", "Berbatov"));

        sessionService.add("12312", session);

        response.addHeader("Set-Cookie", "sessionId=12312");
        return "/pages/index.html";
    }
}
