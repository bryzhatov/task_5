package ua.griddynamics.geekshop.controllers;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import ua.griddynamics.geekshop.Application;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.io.*;
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

    public void getResources(HttpRequest request, HttpResponse response) {
        String url = StringUtils.splitByWholeSeparator(request.getUrl(), "/static/")[0];
        InputStream inputStream = Application.class.getResourceAsStream("/web/static/" + url);

        if (inputStream != null) {
            try (Reader stream = new BufferedReader(new InputStreamReader(inputStream))) {
                response.write(stream);
            } catch (IOException e) {
                // TODO will do set code in Response
                log.error("Can't read static resources: " + e);
            }
        }
    }
}
