package ua.griddynamics.httpserver.tasks.http;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.entity.Request;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-01-17
 */
@Log4j
public class KeepAliveHttpTask extends HttpTask {
    private final HttpServer httpServer;
    private final Request request;

    public KeepAliveHttpTask(HttpServer httpServer, Request request) {
        super(httpServer);
        this.request = request;
        this.httpServer = httpServer;
    }

    @Override
    public void doTask() {
        try {
            request.getConnection().setKeepAlive(true);
            request.getConnection().setSoTimeout(1000 * keepAliveTimeOut);

            try {
                httpServer.getRequestService().parse(request);

                if (request.getHeaders().size() != 0) {
                    httpServer.getRequestThreadPool().handle(new RequestHttpTask(httpServer, request));
                }
            } catch (IOException e) {
                log.debug("Error when blocking on I/O operation.", e);
                closeConnection(request);
            }
        } catch (SocketException e) {
            log.error("Error when setting keep-alive for connection.", e);
            closeConnection(request);
        }
    }
}
