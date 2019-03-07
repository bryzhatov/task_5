package ua.griddynamics.geekshop.service;

import lombok.Setter;
import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.util.json.CategoryTree;

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

    public Category getCategory(int id) {
        return categoryRepository.getCategory(id);
    }

    public List<CategoryTree> getCategories(int deep, int categoryId) {
        return categoryRepository.getCategories(deep, categoryId);
    }
}
