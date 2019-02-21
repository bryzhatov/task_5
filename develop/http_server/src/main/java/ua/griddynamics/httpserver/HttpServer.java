package ua.griddynamics.httpserver;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.httpserver.api.Reaction;
import ua.griddynamics.httpserver.api.Server;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;
import ua.griddynamics.httpserver.properties.HttpServerProperties;
import ua.griddynamics.httpserver.pool.ThreadPool;
import ua.griddynamics.httpserver.pool.util.ThreadPoolDTO;
import ua.griddynamics.httpserver.pool.factory.ThreadPoolFactory;
import ua.griddynamics.httpserver.service.RequestService;
import ua.griddynamics.httpserver.service.ResponseService;
import ua.griddynamics.httpserver.tasks.http.RequestHttpTask;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
@Data
@Log4j
public class HttpServer implements Server {
    private final Map<String, Map<String, Reaction>> reactionMap = new ConcurrentHashMap<>();
    private final RequestService requestService = new RequestService();
    private final ServerSocket socketServer = new ServerSocket();
    private final ResponseService responseService;
    private final HttpServerProperties propServer;
    private final ThreadPool keepAliveThreadPool;
    private final ThreadPool requestThreadPool;

    public HttpServer(HttpServerConfig config) throws IOException {
        propServer = new HttpServerProperties(config);

        responseService = new ResponseService(propServer);

        keepAliveThreadPool = new ThreadPoolFactory(getKeepAlivePoolDTO(propServer))
                .getPool(propServer.getPoolType());
        requestThreadPool = new ThreadPoolFactory(getRequestPoolDTO(propServer))
                .getPool(propServer.getPoolType());
    }

    @Override
    public void deploy() {
        try {
            socketServer.bind(new InetSocketAddress(propServer.getHost(), propServer.getPort()));
            log.info("Deployed by: " + propServer.getHost() + ":" + propServer.getPort());
            socketServer.setSoTimeout(1000);
        } catch (SocketException e) {
            log.error("Error when set time out for server: " + e);
        } catch (IOException e) {
            log.error("Error deploy: " + e);
        }

        while (!Thread.currentThread().isInterrupted()) {
            Socket connection = null;
            try {
                connection = socketServer.accept();

                RequestHttpTask requestHttpTask = new RequestHttpTask(this, connection);
                requestThreadPool.handle(requestHttpTask);

            } catch (SocketTimeoutException e) {
                if (Thread.currentThread().isInterrupted()) {
                    close();
                }
            } catch (IOException e) {
                log.error("Error with new connection: " + e);
                connectionClose(connection);
            }
        }
    }

    @Override
    public void close() {
        try {
            requestThreadPool.shutdownNow();
            keepAliveThreadPool.shutdownNow();

            if (socketServer.isClosed()) {
                socketServer.close();
            }
            log.info("HttpServer by http:/" + socketServer.getLocalSocketAddress() + " was closed.");
        } catch (IOException e) {
            log.error("Error when close HttpServer: " + e);
        }
    }

    @Override
    public void addReaction(String url, String method, Reaction reaction) {
        Map<String, Reaction> valueReactionMap = reactionMap.computeIfAbsent(url, k -> new ConcurrentHashMap<>());
        valueReactionMap.put(method, reaction);
    }

    private void connectionClose(Socket connection) {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.getOutputStream().close();
                connection.getInputStream().close();
                connection.close();
            } catch (IOException se) {
                log.error("Error with close socket connection: " + se);
            }
        }
    }

    private ThreadPoolDTO getRequestPoolDTO(HttpServerProperties properties) {
        ThreadPoolDTO threadPoolDTO = new ThreadPoolDTO();
        threadPoolDTO.setCorePoolSize(properties.getCoreRequestPoolSize());
        threadPoolDTO.setMaxPoolSize(properties.getMaxRequestPoolSize());
        threadPoolDTO.setKeepAliveTime(properties.getTimeIdleRequestPool());
        return threadPoolDTO;
    }

    private ThreadPoolDTO getKeepAlivePoolDTO(HttpServerProperties properties) {
        ThreadPoolDTO threadPoolDTO = new ThreadPoolDTO();
        threadPoolDTO.setCorePoolSize(properties.getCoreKeepAlivePoolSize());
        threadPoolDTO.setMaxPoolSize(properties.getMaxKeepAlivePoolSize());
        threadPoolDTO.setKeepAliveTime(properties.getTimeIdleKeepAlivePool());
        return threadPoolDTO;
    }
}
