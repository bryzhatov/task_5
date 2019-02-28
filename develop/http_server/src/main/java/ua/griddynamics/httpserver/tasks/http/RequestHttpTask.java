package ua.griddynamics.httpserver.tasks.http;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.Reaction;
import ua.griddynamics.httpserver.api.controller.RequestMethods;
import ua.griddynamics.httpserver.entity.Request;
import ua.griddynamics.httpserver.entity.Response;
import ua.griddynamics.httpserver.exception.ServerException;

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
        try {
            if (request == null) {
                try {
                    request = new Request(connection);
                    connection.setKeepAlive(true);
                    connection.setSoTimeout(emptyRequestTimeOut);
                    httpServer.getRequestService().parse(request);
                } catch (IOException e) {
                    log.debug("Error when parsing request", e);
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
        } catch (ServerException e) {
            try {
                Response response = new Response(request);
                response.setStatus(e.getStatusCode());
                httpServer.getResponseService().respond(request, response);
            } catch (IOException e1) {
                log.error("Can't response about error", e1);
            }
        }
    }

    private void returnResponse(Request request) throws IOException {
        Response response = new Response(request);

        Reaction reaction = getReaction(request, request.getMethod());

        if (reaction != null) {
            reaction.react(request, response);
        }

        httpServer.getResponseService().respond(request, response);
    }

    private Reaction getReaction(Request request, RequestMethods method) {
        Lock readLock = httpServer.getLockPatternMap().readLock();
        try {
            readLock.lock();
            for (Map.Entry<String, Map<RequestMethods, Reaction>> entry : httpServer.getPatternMap().entrySet()) {

                if (StringUtils.startsWith(request.getUrl(), entry.getKey())) {

                    request.setPathInfo(StringUtils.removeStart(request.getUrl(), entry.getKey()));

                    return entry.getValue().get(method);
                }
            }
        } finally {
            readLock.unlock();
        }

        Map<RequestMethods, Reaction> reactions = httpServer.getReactionMap().get(request.getUrl());

        if (reactions != null) {
            return reactions.get(method);
        }

        return null;
    }
}
