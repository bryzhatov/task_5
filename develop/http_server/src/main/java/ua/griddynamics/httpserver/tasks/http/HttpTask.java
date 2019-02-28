package ua.griddynamics.httpserver.tasks.http;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.entity.Request;
import ua.griddynamics.httpserver.tasks.Task;

import java.io.IOException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-01-17
 */
@Log4j
public abstract class HttpTask implements Task {
    protected final HttpServer httpServer;
    protected final int keepAliveTimeOut;

    protected HttpTask(HttpServer httpServer) {
        this.keepAliveTimeOut = httpServer.getPropServer().getKeepAliveTimeout();
        this.httpServer = httpServer;
    }

    void closeConnection(Request request) {
        try {
            request.getReaderStream().close();
            request.getWriterStream().close();
            request.getConnection().close();
        } catch (IOException e) {
            log.error("Error when close connection.", e);
        }
    }
}
