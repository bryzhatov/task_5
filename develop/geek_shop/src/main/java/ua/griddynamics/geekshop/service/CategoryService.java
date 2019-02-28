package ua.griddynamics.geekshop.service;

import lombok.Setter;
import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class CategoryService {
    @Setter
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void m() {

    }

    public List<Category> getMainCategories() throws ServiceException {
        try {
            return categoryRepository.getMainCategories();
        } catch (DataBaseException e) {
            throw new ServiceException("Can't get main categories", e);
        }
    }

    public List<Category> getCategories(int parentId, int deep) throws ServiceException {
        try {
            return categoryRepository.getAllCategories();
        } catch (DataBaseException e) {
            throw new ServiceException("Can't get all categories", e);
        }
    }
}
