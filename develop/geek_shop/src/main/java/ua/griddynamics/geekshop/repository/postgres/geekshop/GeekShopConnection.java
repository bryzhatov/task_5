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
}
