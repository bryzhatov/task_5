package ua.griddynamics.geekshop.controllers;

import lombok.Data;
import ua.griddynamics.geekshop.controllers.entity.Model;
import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.geekshop.service.UserService;
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
@Data
public class AuthController {
    private SessionService sessionService;
    private UserService userService;

    public AuthController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public String auth(HttpRequest request, HttpResponse response, Model model) {
        String[] mass = new String(request.getBody()).split("[=&]");
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < mass.length - 1; i++) {
            map.put(mass[i], mass[i + 1]);
        }

        String login = map.get("login");
        String password = map.get("password");

        User user = userService.get(login, password);

        if (user != null) {
            Session session = new Session();
            session.add("user", user);
            sessionService.add(String.valueOf(user.hashCode()), session);
            response.addHeader("Set-Cookie", "sessionId=" + user.hashCode());
        }
        return "/pages/index.html";
    }
}
