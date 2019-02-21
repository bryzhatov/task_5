package ua.griddynamics.geekshop.repository.postgres.geekshop;

import lombok.extern.log4j.Log4j;
import org.postgresql.ds.PGConnectionPoolDataSource;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.postgres.PostgresConnection;

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
public class GeekShopConnection implements PostgresConnection {
    private final PGConnectionPoolDataSource dataSource = new PGConnectionPoolDataSource();

    public GeekShopConnection() {
        initDataSource(getProperties("app/geek_shop_db.properties"));
    }

    public GeekShopConnection(Properties properties) {
        initDataSource(properties);
    }

    @Override
    public Connection getConnection() throws DataBaseException {
        try {
            return dataSource.getPooledConnection().getConnection();
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private void initDataSource(Properties properties) {
        dataSource.setDatabaseName(properties.getProperty("name"));
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));
        dataSource.setPortNumber(Integer.parseInt(properties.getProperty("port")));
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
