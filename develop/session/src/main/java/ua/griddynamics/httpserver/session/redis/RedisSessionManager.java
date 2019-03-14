package ua.griddynamics.httpserver.session.redis;

import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionManager;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class RedisSessionManager implements SessionManager {

    @Override
    public void add(String id, Session session) {

    }

    @Override
    public void add(String id, Session session, long seconds) {

    }

    @Override
    public Session get(String id) {
        return null;
    }

    @Override
    public void invalidate(Session session) {

    }
}
