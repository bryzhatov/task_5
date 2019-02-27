package ua.griddynamics.httpserver.utils.controllers;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
@Log4j
public class StaticResourceController {
    private final Map<String, byte[]> cache = new ConcurrentHashMap<>();
    private final Path staticResDir;

    public StaticResourceController(Path staticResDir) {
        this.staticResDir = staticResDir;
    }

    public void getResources(HttpRequest request, HttpResponse response) {
        try {
            byte[] byteFile;
            byteFile = cache.get(request.getPathInfo());

            if (byteFile != null) {
                response.write(new String(byteFile));
            } else {
                Files.walk(staticResDir)
                        .filter(Files::isRegularFile)
                        .forEach((path) ->
                        {
                            if (path.toString().endsWith(request.getPathInfo())) {
                                try {
                                    byte[] temp = Files.readAllBytes(path);
                                    cacheFile(request.getPathInfo(), temp);
                                    response.write(new String(temp));
                                } catch (IOException e) {
                                    log.error("Can't read bytes from resource", e);
                                }
                            }
                        });
            }
        } catch (IOException e) {
            log.error("Can't watch static resource directory", e);
        }
    }

    private void cacheFile(String file, byte[] bytesFile) {
        cache.put(file, bytesFile);
    }
}
