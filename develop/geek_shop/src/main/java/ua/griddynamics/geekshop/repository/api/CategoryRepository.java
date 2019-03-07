package ua.griddynamics.geekshop.repository.api;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.util.json.CategoryTree;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface CategoryRepository {

    Category getCategory(int id);

    List<CategoryTree> getCategories(int deep, int categoryId);
}
