package ua.griddynamics.httpserver.session.redis.repo;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionRepository;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class SessionRepositoryRedis implements SessionRepository {
    private final Jedis jedis;
    private final Gson gson = new Gson();

    public SessionRepositoryRedis(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void add(String id, Session session) {
        jedis.append(id, gson.toJson(session));
    }

    @Override
    public void add(String id, Session session, int seconds) {
        jedis.set(id, gson.toJson(session), SetParams.setParams().ex(seconds));
    }

    @Override
    public Session get(String id) {
        return gson.fromJson(jedis.get(id), Session.class);
    }
}