package ua.griddynamics.geekshop.repository.api;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.entity.util.CategoryTree;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface CategoryRepository {
    List<Category> getMainCategories();

    List<CategoryTree> getCategories(int deep, int categoryId);
}
