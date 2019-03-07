package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.util.json.CategoryTree;

import java.sql.*;
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
    public Category getCategory(int id) {
        try (Connection connection = connectionSupplier.get()) {

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM categories WHERE id = ?")) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return categoryMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
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

                findChild(mapCategoriesTree, deep);

            } else {
                CategoryTree categoryTree = new CategoryTree(getCategory(categoryId));
                categoryTrees.add(categoryTree);

                findChild(Collections.singletonMap(categoryId, categoryTree), deep);
            }

            return categoryTrees;
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    private void findChild(Map<Integer, CategoryTree> categoryMap, int deep) throws SQLException {
        if (deep > 1 && categoryMap.size() > 0) {
            try (Connection connection = connectionSupplier.get()) {
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(buildSql(categoryMap))) {
                        Map<Integer, CategoryTree> childCategoryMap = new HashMap<>();
                        while (resultSet.next()) {

                            Category category = categoryMapper(resultSet);
                            CategoryTree categoryTree = new CategoryTree(category);
                            childCategoryMap.put(category.getId(), categoryTree);

                            categoryMap.get(category.getParentId()).addChildren(categoryTree);
                        }
                        findChild(childCategoryMap, --deep);
                    }
                }
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
