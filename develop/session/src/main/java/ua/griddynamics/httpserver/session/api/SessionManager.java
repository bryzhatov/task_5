package ua.griddynamics.httpserver.session.api;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public interface SessionManager {
    void add(Session session);
    void add(Session session, long seconds);
    Session get(String key);
    void invalidate(Session session);
}
