package ua.griddynamics.geekshop.repository.postgres.geekshop;

import lombok.extern.log4j.Log4j;
import org.apache.commons.dbcp2.BasicDataSource;
import ua.griddynamics.geekshop.exception.DataBaseException;

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
    private final BasicDataSource poolConnection;

    public GeekShopConnectionProvider(Properties properties) {
        poolConnection = getPooledConnection(properties);
    }

    public Connection get() {
        try {
            return poolConnection.getConnection();
        } catch (SQLException e) {
            throw new DataBaseException("Can't get connection", e);
        }
    }

    private BasicDataSource getPooledConnection(Properties properties) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(properties.getProperty("db.driver"));
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.user"));
        dataSource.setPassword(properties.getProperty("db.password"));


        dataSource.setInitialSize(Integer.parseInt(properties.getProperty("db.initSizePool")));
        dataSource.setMaxTotal(Integer.parseInt(properties.getProperty("db.maxSizePool")));
        dataSource.setMaxIdle(Integer.parseInt(properties.getProperty("db.maxIdleSizePool")));
        dataSource.setMinIdle(Integer.parseInt(properties.getProperty("db.minIdleSizePool")));
        dataSource.setMaxWaitMillis(Integer.parseInt(properties.getProperty("db.maxWaitMillisConnection")));
        return dataSource;
    }
}
