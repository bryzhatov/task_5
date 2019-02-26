package ua.griddynamics.httpserver.api;

import ua.griddynamics.httpserver.api.controller.RequestMethods;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-15
 */
public interface Server {
    void deploy();

    void close();

    void addReaction(String url, RequestMethods method, Reaction reaction);
}
