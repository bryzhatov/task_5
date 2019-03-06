package ua.griddynamics.httpserver.api;


import ua.griddynamics.httpserver.api.exception.ReactionException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
@FunctionalInterface
public interface Reaction {
    void react(HttpRequest request, HttpResponse response) throws ReactionException;
}
