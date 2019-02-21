package ua.griddynamics.geekshop.repository;

import ua.griddynamics.geekshop.entity.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class DtoUtils {
    public static Category getCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getInt("id"));
        category.setParentId(resultSet.getInt("parent_id"));
        category.setName(resultSet.getString("name"));
        return category;
    }
}
