package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.entity.util.CategoryTree;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
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
    public List<CategoryTree> getCategories(int deep, int categoryId) {
        try (Connection connection = connectionSupplier.get()) {

            List<CategoryTree> categoryTrees = new ArrayList<>();

            if (categoryId == 0) {
                List<Category> categories = getMainCategories(connection);
                Map<Integer, CategoryTree> mapCategoriesTree = new HashMap<>();

                for (Category category : categories) {
                    CategoryTree categoryTree = new CategoryTree(category);
                    categoryTrees.add(categoryTree);
                    mapCategoriesTree.put(category.getId(), categoryTree);
                }

                findWidth(mapCategoriesTree, deep);

            } else {
                CategoryTree categoryTree = new CategoryTree(new Category(categoryId, "", 0));
                categoryTrees.add(categoryTree);
                findWidth(Collections.singletonMap(categoryId, categoryTree), deep);
            }

            return categoryTrees;
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    public void findWidth(Map<Integer, CategoryTree> categoryTrees, int deep) {
        if (deep > 1) {
            try (Connection connection = connectionSupplier.get()) {
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(buildSql(categoryTrees))) {
                        Map<Integer, CategoryTree> map = new HashMap<>();
                        while (resultSet.next()) {
                            Category category = categoryMapper(resultSet);
                            CategoryTree categoryTree = new CategoryTree(category);
                            categoryTrees.get(category.getParentId()).addChildren(categoryTree);
                            map.put(category.getId(), categoryTree);
                        }
                        findWidth(map, --deep);
                    }
                }
            } catch (SQLException e) {

            }
        }
    }

    private String buildSql(Map<Integer, CategoryTree> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM categories WHERE parent_id IN (");

        Iterator<Map.Entry<Integer, CategoryTree>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().getKey());
            if (iterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public List<Category> getMainCategories() throws DataBaseException {
        try (Connection connection = connectionSupplier.get()) {
            return getMainCategories(connection);
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    private List<Category> getMainCategories(Connection connection) {
        List<Category> categories = new ArrayList<>();

        try (ResultSet resultSet = connection
                .createStatement().executeQuery("SELECT * FROM categories WHERE parent_id = 0")) {

            if (resultSet != null) {
                while (resultSet.next()) {
                    Category category = categoryMapper(resultSet);
                    categories.add(category);
                }
                return categories;
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
