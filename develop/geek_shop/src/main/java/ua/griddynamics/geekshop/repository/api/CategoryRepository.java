package ua.griddynamics.geekshop.repository.api;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface CategoryRepository {
    List<Category> getAllCategories() throws DataBaseException;

    List<Category> getMainCategories() throws DataBaseException;
}
