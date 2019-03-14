package ua.griddynamics.httpserver.session.redis.repo;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import ua.griddynamics.httpserver.session.api.Session;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class SessionRepositoryRedis {
    private final Jedis jedis;
    private final Gson gson = new Gson();

    public SessionRepositoryRedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public void add(String id, Session session) {
        jedis.append(id, gson.toJson(session));
    }

    public void add(String id, Session session, int seconds) {
        jedis.set(id, gson.toJson(session), SetParams.setParams().ex(seconds));
    }

    public Session get(String id) {
        return gson.fromJson(jedis.get(id), Session.class);
    }
}
