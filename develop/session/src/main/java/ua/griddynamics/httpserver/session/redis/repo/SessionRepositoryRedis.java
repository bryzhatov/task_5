package ua.griddynamics.httpserver.session.redis.repo;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import ua.griddynamics.httpserver.session.api.Meta;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionRepository;

import java.io.IOException;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
public class SessionRepositoryRedis implements SessionRepository {
    private final Gson gson = new Gson();
    private final Jedis jedis;

    public SessionRepositoryRedis(Jedis jedis) throws IOException {
        this.jedis = jedis;
    }

    @Override
    public void add(String id, Session session) {
        jedis.append(id, gson.toJson(session, Session.class));
    }

    @Override
    public void add(String id, Session session, int seconds) {
        jedis.set(id, gson.toJson(seconds, Session.class), SetParams.setParams().ex(seconds));
    }

    @Override
    public Session get(String id) {
        Session session = null;
        try {
            String jsonSession = jedis.get(id);

            if (jsonSession != null) {
                session = gson.fromJson(jedis.get(id), Session.class);
                String sessionJson = gson.toJson(session);

                Session se = gson.fromJson(sessionJson, Session.class);

                for (Map.Entry<String, Meta> entry : se.getValues().entrySet()) {
                    String jsonObject = entry.getValue().getObject().toString().replace("=", ":");
                    Object object = gson.fromJson(jsonObject, Class.forName(entry.getValue().getClassName()));
                    entry.getValue().setObject(object);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
        return session;
    }

    @Override
    public void remove(String id) {
        jedis.del(id);
    }
}
