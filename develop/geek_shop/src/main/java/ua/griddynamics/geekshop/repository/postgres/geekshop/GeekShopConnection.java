package ua.griddynamics.geekshop.repository.postgres.geekshop;

import lombok.extern.log4j.Log4j;
import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class GeekShopConnection {
    private final PooledConnection pooledConnection;

    public GeekShopConnection(String path) {
        pooledConnection = getPooledConnection(getProperties(path));
    }

    public GeekShopConnection(Properties properties) {
        pooledConnection = getPooledConnection(properties);
    }

    public Connection getConnection() {
        try {
            return pooledConnection.getConnection();
        } catch (SQLException e) {
            log.error("Can't get connection: " + e);
        }
        return null;
    }

    private PooledConnection getPooledConnection(Properties properties) {
        try {
            PGConnectionPoolDataSource dataSource = new PGConnectionPoolDataSource();
            dataSource.setDatabaseName(properties.getProperty("name"));
            dataSource.setUser(properties.getProperty("user"));
            dataSource.setPassword(properties.getProperty("password"));
            dataSource.setPortNumber(Integer.parseInt(properties.getProperty("port")));

            return dataSource.getPooledConnection();
        } catch (SQLException e) {
            log.error("Can't get pooled of connections: " + e);
        }
        return null;
    }

    private Properties getProperties(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(name)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            log.error("Can't load DB properties: " + e);
        }
        return properties;
    }
}
