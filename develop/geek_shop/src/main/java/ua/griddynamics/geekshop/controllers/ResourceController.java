package ua.griddynamics.geekshop.controllers;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import ua.griddynamics.geekshop.Application;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
@Log4j
public class ResourceController {
    private final Map<String, byte[]> cache = new ConcurrentHashMap<>();

    public void getResources(HttpRequest request, HttpResponse response) {
        byte[] cacheFile = cache.get(request.getUrl());

        if (cacheFile == null) {
            String url = StringUtils.splitByWholeSeparator(request.getUrl(), "/static/")[0];
            try (InputStream inputStream = Application.class.getResourceAsStream("/web/static/" + url)) {
                cacheFile = readAllBytes(inputStream);
                cache.put(url, cacheFile);
            } catch (IOException e) {
                // TODO code, message
                log.error("");
            }
        }

        response.write(new String(cacheFile));
    }

    private byte[] readAllBytes(InputStream stream) {
        List<Byte> list = new ArrayList<>();
        int i;
        try {
            while ((i = stream.read()) != -1) {
                list.add((byte) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] mass = new byte[list.size()];

        for (int a = 0; a < list.size(); a++) {
            mass[a] = list.get(a);
        }

        return mass;
    }
}
