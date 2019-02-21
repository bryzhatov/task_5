package ua.griddynamics.geekshop.repository.api;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface CategoryRepository {

    int create(Category category) throws DataBaseException;

    Category get(int id) throws DataBaseException;

    boolean update(Category category) throws DataBaseException;

    boolean delete(int id) throws DataBaseException;

    List<Category> getCategories() throws DataBaseException;

    List<Category> getMainCategories() throws DataBaseException;
}
