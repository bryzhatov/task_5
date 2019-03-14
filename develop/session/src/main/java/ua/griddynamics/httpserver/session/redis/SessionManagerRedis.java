package ua.griddynamics.httpserver.session.redis;

import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionManager;
import ua.griddynamics.httpserver.session.redis.repo.SessionRepositoryRedis;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class SessionManagerRedis implements SessionManager {
    private final SessionRepositoryRedis sessionRepositoryRedis;

    public SessionManagerRedis(SessionRepositoryRedis sessionRepositoryRedis) {
        this.sessionRepositoryRedis = sessionRepositoryRedis;
    }

    @Override
    public void add(String id, Session session) {
        sessionRepositoryRedis.add(id, session);
    }

    @Override
    public void add(String id, Session session, int seconds) {
        sessionRepositoryRedis.add(id, session, seconds);
    }

    @Override
    public Session get(String id) {
        return null;
    }

    @Override
    public void invalidate(Session session) {

    }
}
