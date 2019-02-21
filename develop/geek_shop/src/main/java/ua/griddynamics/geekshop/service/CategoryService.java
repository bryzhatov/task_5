package ua.griddynamics.geekshop.service;

import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(RepositoryFacade repositoryFacade) {
        categoryRepository = repositoryFacade.getCategoryPostgres();
    }

    public List<Category> getMainCategories() throws ServiceException {
        try {
            return categoryRepository.getMainCategories();
        } catch (DataBaseException e) {
            throw new ServiceException("Can't get main categories: " + e);
        }
    }

    public List<Category> getCategories() throws ServiceException {
        try {
            return categoryRepository.getCategories();
        } catch (DataBaseException e) {
            throw new ServiceException("Can't get all categories: " + e);
        }
    }
}
