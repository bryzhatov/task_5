package ua.griddynamics.geekshop.controllers;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
@Log4j
public class ResourceController {

    public void getCss(HttpRequest request, HttpResponse response) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource("web/in.css");
            Path path = Paths.get(url.toURI());

            response.getWriter().write(new String(Files.readAllBytes(path)));
        } catch (URISyntaxException | IOException e) {
            log.error("Can't load web/in.css: " + e);
        }
    }
}
