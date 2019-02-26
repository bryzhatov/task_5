package ua.griddynamics.httpserver.tasks.http;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.Reaction;
import ua.griddynamics.httpserver.api.controller.RequestMethods;
import ua.griddynamics.httpserver.entity.Request;
import ua.griddynamics.httpserver.entity.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-01-17
 */
@Log4j
public class RequestHttpTask extends HttpTask {
    private final int emptyRequestTimeOut;
    private final boolean serverKeepAlive;
    private Socket connection;
    private Request request;

    private RequestHttpTask(HttpServer httpServer) {
        super(httpServer);
        this.emptyRequestTimeOut = httpServer.getPropServer().getEmptyRequestTimeOut();
        this.serverKeepAlive = httpServer.getPropServer().isKeepAliveOn();
    }

    public RequestHttpTask(HttpServer httpServer, Socket connection) {
        this(httpServer);
        this.connection = connection;
    }

    public RequestHttpTask(HttpServer httpServer, Request request) {
        this(httpServer);
        this.request = request;
    }

    @Override
    public void doTask() {
        if (request == null) {
            try {
                request = new Request(connection);
                connection.setKeepAlive(true);
                connection.setSoTimeout(emptyRequestTimeOut);
                httpServer.getRequestService().parse(request);
            } catch (IOException e) {
                log.debug(Thread.currentThread().getName() + ": Error when parsing request: " + e);
            }
        }

        if (request.isCorrect()) {
            if (httpServer.getPropServer().isVisibleRequest()) {
                log.info(request.getMethod().name() + " " + request.getUrl());
            }

            try {
                returnResponse(request);

                if (request.isClose()) {
                    closeConnection(request);
                }

                if (serverKeepAlive && request.isKeepAlive()) {
                    httpServer.getKeepAliveThreadPool().handle(new KeepAliveHttpTask(httpServer, request));
                }
            } catch (IOException e) {
                log.error("Error when response: " + e);
                closeConnection(request);
            }
        } else {
            log.debug("Request is not correct: " + request);
        }
    }

    private void returnResponse(Request request) throws IOException {
        Response response = new Response(request);

        Reaction reaction = getReaction(request.getUrl(), request.getMethod());

        if (reaction != null) {
            reaction.react(request, response);
        }

        httpServer.getResponseService().respond(request, response);
    }

    private Reaction getReaction(String url, RequestMethods method) {
        Lock readLock = httpServer.getReadWriteLockPatternMap().readLock();
        try {
            readLock.tryLock();
            for (Map.Entry<String, Map<RequestMethods, Reaction>> entry : httpServer.getPatternMap().entrySet()) {
                    if (StringUtils.startsWith(url, entry.getKey())) {
                        return entry.getValue().get(method);
                    }
                }
        } finally {
            readLock.unlock();
        }


        Map<RequestMethods, Reaction> reactions = httpServer.getReactionMap().get(url);

        if (reactions != null) {
            return reactions.get(method);
        }

        return null;
    }
}
