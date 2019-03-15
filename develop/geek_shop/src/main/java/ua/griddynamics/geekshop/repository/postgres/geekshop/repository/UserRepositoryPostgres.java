package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-15
 */
public class UserRepositoryPostgres implements UserRepository {
    private final Supplier<Connection> connectionSupplier;

    public UserRepositoryPostgres(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public User get(int id) {
        try (Connection connection = connectionSupplier.get()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return userMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    @Override
    public User get(String login, String password) {
        try (Connection connection = connectionSupplier.get()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM \"user\" WHERE login = ? AND password = ?")) {
                statement.setString(1, login);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return userMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    private User userMapper(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String surName = resultSet.getString("sur_name");
        String password = resultSet.getString("password");
        return new User(id, name, surName, password);
    }
}
