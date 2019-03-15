package ua.griddynamics.httpserver.session.api;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public interface SessionService {
    void add(String id, Session session);
    void add(String id, Session session, int seconds);
    Session get(String id);
    void invalidate(Session session);
}
