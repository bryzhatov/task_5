package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class CategoryPostgresRepository implements CategoryRepository {
    private final Supplier<Connection> connectionSupplier;

    public CategoryPostgresRepository(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public List<Category> getAllCategories() throws DataBaseException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionSupplier.get()) {

            try (ResultSet resultSet = connection
                    .createStatement().executeQuery("SELECT * FROM \"categories\" ORDER BY parent_id")) {

                if (resultSet != null) {
                    while (resultSet.next()) {
                        Category category = categoryMapper(resultSet);
                        categories.add(category);
                    }
                    return categories;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
        return categories;
    }

    @Override
    public List<Category> getMainCategories() throws DataBaseException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionSupplier.get()) {

            try (ResultSet resultSet = connection
                    .createStatement().executeQuery("SELECT * FROM \"category\" WHERE parent_id IS NULL")) {

                if (resultSet != null) {
                    while (resultSet.next()) {
                        Category category = categoryMapper(resultSet);
                        categories.add(category);
                    }
                    return categories;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
        return categories;
    }

    private Category categoryMapper(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int parentId = resultSet.getInt("parent_id");
        String name = resultSet.getString("name");
        return new Category(id, name, parentId);
    }
}
