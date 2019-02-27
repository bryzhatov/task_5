package ua.griddynamics.httpserver.service;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.httpserver.entity.Request;
import ua.griddynamics.httpserver.entity.Response;
import ua.griddynamics.httpserver.properties.HttpServerProperties;
import ua.griddynamics.httpserver.utils.HttpCodes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
@Log4j
public class ResponseService {
    private final ThreadLocal<DateTimeFormatter> threadLocal = ThreadLocal.withInitial(
            () -> DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GTM'", Locale.ENGLISH));
    private final HttpCodes httpCodes = new HttpCodes();
    private final ErrorPage errorPage = new ErrorPage();
    private final HttpServerProperties serverProp;

    public ResponseService(HttpServerProperties serverProp) {
        this.serverProp = serverProp;
    }

    public void respond(Request request, Response response) throws IOException {
        setStatusCode(response);
        prepareResponse(request, response);
        writeResponse(request, response);
    }

    private void setStatusCode(Response response) {
        if (response.getWriter().getBuffer().length() == 0 && response.getStatus() == 0) {
            response.setStatus(404);
        }
        if (response.getWriter().getBuffer().length() > 0 && response.getStatus() == 0) {
            response.setStatus(200);
        }
    }

    private void prepareResponse(Request request, Response response) {
        if (request.isKeepAlive()) {
            response.addHeaderIfAbsent("Connection", "Keep-Alive");
            response.addHeaderIfAbsent("Keep-Alive", "timeout=" + serverProp.getKeepAliveTimeout() +
                    ", max=" + serverProp.getKeepAliveTransactional());
        } else {
            response.addHeaderIfAbsent("Connection", "close");
        }
        response.addHeaderIfAbsent("Content-Length", String.valueOf(getBody(response).getBytes().length + 2));
        response.addHeaderIfAbsent("Date", threadLocal.get().format(LocalDateTime.now()));
        response.addHeaderIfAbsent("Location", request.getLocation());
        response.addHeaderIfAbsent("Server", "Anton/0.1");
    }

    private void writeResponse(Request request, Response response) throws IOException {
        BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(response.getSocket().getOutputStream()));

        String status = String.format("HTTP/1.1 %s %s\r\n", response.getStatus(), httpCodes.getMessage(response.getStatus()));
        writer.write(status);

        StringBuilder headersBuilder = new StringBuilder();
        for (Map.Entry entry : response.getHeaders().entrySet()) {
            headersBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        headersBuilder.append("\r\n\r\n");
        writer.write(headersBuilder.toString());

        writer.write(getBody(response));

        if (!request.getConnection().isClosed()) {
            writer.flush();
        } else {
            log.error("Connection closed before write answer.");
        }
    }

    private String getBody(Response response) {
        if (response.getWriter().getBuffer().length() > 0) {
            return response.getWriter().toString();
        } else {
            return errorPage.getErrorPage(response.getStatus());
        }
    }

    private class ErrorPage {
        String getErrorPage(int status) {
            return "<html><title></title><body><h1><b>" +
                    status + " " +
                    httpCodes.getMessage(status) +
                    "</b><h1></body></html>";
        }
    }
}
