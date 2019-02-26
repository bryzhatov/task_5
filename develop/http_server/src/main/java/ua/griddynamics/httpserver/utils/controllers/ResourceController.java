package ua.griddynamics.httpserver.utils.controllers;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
@Log4j
public class ResourceController {
    private final ReadWriteLock readWriteLockPatternMap = new ReentrantReadWriteLock(true);
    private final Map<String, byte[]> cache = new HashMap<>();
    private final Path staticResDir;

    public ResourceController(Path staticResDir) {
        this.staticResDir = staticResDir;
    }

    public void getResources(HttpRequest request, HttpResponse response) {
        try {
            String findResName = StringUtils.splitByWholeSeparator(request.getUrl(), "/static/")[0];

            Lock lock = readWriteLockPatternMap.readLock();
            byte[] byteFile;
            try {
                lock.tryLock();
                byteFile = cache.get(findResName);
            } finally {
                lock.unlock();
            }

            if (byteFile != null) {
                response.write(new String(byteFile));
            } else {
                Files.walk(staticResDir)
                        .filter(Files::isRegularFile)
                        .forEach((path) ->
                        {
                            if (path.toString().endsWith(findResName)) {
                                try {
                                    byte[] temp = Files.readAllBytes(path);
                                    cacheFile(findResName, temp);
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
        Lock lock = readWriteLockPatternMap.writeLock();
        try {
            lock.tryLock();
            cache.put(file, bytesFile);
        } finally {
            lock.unlock();
        }
    }
}
