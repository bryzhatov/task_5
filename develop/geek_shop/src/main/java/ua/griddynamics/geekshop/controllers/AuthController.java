package ua.griddynamics.geekshop.controllers;

import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.session.api.SessionManager;

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
        String login = request.getParameter("login");
        String password = request.getParameter("passв word");

        User user = new User();
    }
}
