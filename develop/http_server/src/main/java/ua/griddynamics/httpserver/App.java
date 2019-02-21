package ua.griddynamics.httpserver;

import ua.griddynamics.httpserver.api.config.HttpServerConfig;

import java.io.IOException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
public class App {
    public static void main(String[] args) throws IOException {
        HttpServerConfig config = new HttpServerConfig();
        config.setPort(8080);
        HttpServer httpServer = new HttpServer(config);

        httpServer.addReaction("/cat/dog", "GET",
                ((request, response) -> System.out.println("ahaha")));

        httpServer.deploy();
    }
}
