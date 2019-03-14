package ua.griddynamics.httpserver;

import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.api.Reaction;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;
import ua.griddynamics.httpserver.api.controller.RequestMethods;

import java.io.IOException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-14
 */
public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(new HttpServerConfig());
        server.addReaction("/", RequestMethods.GET, new Reaction() {
            @Override
            public void react(HttpRequest request, HttpResponse response) {
                response.addHeader("Set-Cookie", "d=999");
            }
        });
        server.deploy();
    }
}
