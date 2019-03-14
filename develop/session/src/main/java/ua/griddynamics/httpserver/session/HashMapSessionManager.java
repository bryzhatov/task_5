package ua.griddynamics.httpserver.session;

import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-14
 */
public class HashMapSessionManager implements SessionManager {
    private final Map<String, Session> sessionHolder = new ConcurrentHashMap<>();

    @Override
    public void add(String id, Session session) {
        sessionHolder.put(id, session);
    }

    @Override
    public void add(String id, Session session, long seconds) {
        sessionHolder.put(id, session);
    }

    @Override
    public Session get(String key) {
        if(key != null){
            return sessionHolder.get(key);
        }
        return null;
    }

    @Override
    public void invalidate(Session session) {
        sessionHolder.remove(1);
    }
}
