package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.entity.util.CategoryDTO;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public List<CategoryDTO> getCategories(int deep) {
        try (Connection connection = connectionSupplier.get()) {

            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            List<Category> categories = getMainCategories(connection);

            for (Category category : categories) {
                CategoryDTO categoryDTO = new CategoryDTO(category);
                categoryDTOS.add(categoryDTO);

                find(connection, categoryDTO, deep);
            }
            return categoryDTOS;
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    private void find(Connection connection, CategoryDTO parent, int deep) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"categories\" WHERE parent_id = ?")) {
            statement.setInt(1, parent.getCategory().getId());

            if (deep > 1) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        CategoryDTO child = new CategoryDTO(categoryMapper(resultSet));
                        parent.addChildren(child);
                        find(connection, child, --deep);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
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
            return getMainCategories(connection);
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    private List<Category> getMainCategories(Connection connection){
        List<Category> categories = new ArrayList<>();
        try (ResultSet resultSet = connection
                .createStatement().executeQuery("SELECT * FROM \"categories\" WHERE parent_id = 0")) {

            if (resultSet != null) {
                while (resultSet.next()) {
                    Category category = categoryMapper(resultSet);
                    categories.add(category);
                }
                return categories;
            }
        }catch (SQLException e) {
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
