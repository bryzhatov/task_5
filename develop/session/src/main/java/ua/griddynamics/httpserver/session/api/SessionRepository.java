package ua.griddynamics.httpserver.session.api;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-15
 */
public interface SessionRepository {
    void add(String id, Session session);
    void add(String id, Session session, int seconds);
    Session get(String id);
}
