package ua.griddynamics.httpserver.api;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-15
 */
public interface Server {
    void deploy();

    void close();

    void addReaction(String url, String method, Reaction reaction);
}
