package ua.griddynamics.geekshop.repository.postgres.geekshop;

import lombok.extern.log4j.Log4j;
import org.postgresql.ds.PGConnectionPoolDataSource;
import ua.griddynamics.geekshop.exception.DataBaseException;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class GeekShopConnectionProvider implements Supplier<Connection> {
    private final PooledConnection pooledConnection;

    public GeekShopConnectionProvider(Properties properties) {
        pooledConnection = getPooledConnection(properties);
    }

    public Connection get() {
        try {
            return pooledConnection.getConnection();
        } catch (SQLException e) {
            throw new DataBaseException("Can't get connection", e);
        }
    }

    private PooledConnection getPooledConnection(Properties properties) {
        try {
            PGConnectionPoolDataSource dataSource = new PGConnectionPoolDataSource();

            dataSource.setDatabaseName(properties.getProperty("db.name"));
            dataSource.setUser(properties.getProperty("db.user"));
            dataSource.setPassword(properties.getProperty("db.password"));
            dataSource.setPortNumber(Integer.parseInt(properties.getProperty("db.port")));

            return dataSource.getPooledConnection();
        } catch (SQLException e) {
            log.error("Can't get pooled of connections: " + e);
            throw new DataBaseException("Error with connection of data base.", e);
        }
    }
}
