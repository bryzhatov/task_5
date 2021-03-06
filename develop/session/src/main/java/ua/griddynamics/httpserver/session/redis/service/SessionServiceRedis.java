package ua.griddynamics.httpserver.session.redis.service;

import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionRepository;
import ua.griddynamics.httpserver.session.api.SessionService;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class SessionServiceRedis implements SessionService {
    private final SessionRepository sessionRepository;

    public SessionServiceRedis(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void add(String id, Session session) {
        sessionRepository.add(id, session);
    }

    @Override
    public void add(String id, Session session, int seconds) {
        sessionRepository.add(id, session, seconds);
    }

    @Override
    public Session get(String id) {
        return sessionRepository.get(id);
    }

    @Override
    public void invalidate(String id) {
        sessionRepository.remove(id);
    }
}
