package ua.griddynamics.geekshop.service;

import lombok.Setter;
import ua.griddynamics.geekshop.util.json.CategoryTree;
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

    public List<CategoryTree> getCategories(int deep, int parentId) {
        return categoryRepository.getCategories(deep, parentId);
    }
}
