package ua.griddynamics.geekshop.repository.postgres;

import ua.griddynamics.geekshop.exception.DataBaseException;
import java.sql.Connection;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface PostgresConnection {
    Connection getConnection() throws DataBaseException;
}
