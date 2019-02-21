package ua.griddynamics.geekshop.repository.postgres.geekshop;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.DtoUtils;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.repository.postgres.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class CategoryPostgresRepository implements CategoryRepository {
    private final PostgresConnection postgresConnection;

    public CategoryPostgresRepository(PostgresConnection postgresConnection) {
        this.postgresConnection = postgresConnection;
    }

    @Override
    public int create(Category category) throws DataBaseException {
        try (Connection connection = postgresConnection.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO \"category\" (name) VALUES (?) RETURNING id")) {
                statement.setString(1, category.getName());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
        return 0;
    }

    @Override
    public Category get(int id) throws DataBaseException {
        try (Connection connection = postgresConnection.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM \"category\" WHERE id = ?")) {

                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet != null && resultSet.next()) {
                        return DtoUtils.getCategory(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Category category) throws DataBaseException {
        try (Connection connection = postgresConnection.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("UPDATE \"category\" SET name = ? WHERE id = ?")) {

                statement.setString(1, category.getName());
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) throws DataBaseException {
        try (Connection connection = postgresConnection.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM \"category\" WHERE id = ?")) {

                statement.setInt(1, id);
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Override
    public List<Category> getCategories() throws DataBaseException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = postgresConnection.getConnection()) {

            try (ResultSet resultSet = connection
                    .createStatement().executeQuery("SELECT * FROM \"category\"")) {

                if (resultSet != null) {
                    while (resultSet.next()) {
                        Category category = DtoUtils.getCategory(resultSet);
                        categories.add(category);
                    }
                    return categories;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
        return categories;
    }

    @Override
    public List<Category> getMainCategories() throws DataBaseException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = postgresConnection.getConnection()) {

            try (ResultSet resultSet = connection
                    .createStatement().executeQuery("SELECT * FROM \"category\" WHERE parent_id IS NULL")) {

                if (resultSet != null) {
                    while (resultSet.next()) {
                        Category category = DtoUtils.getCategory(resultSet);
                        categories.add(category);
                    }
                    return categories;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
        return categories;
    }
}
