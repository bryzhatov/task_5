package ua.griddynamics.geekshop.entity.util;

import lombok.Data;
import ua.griddynamics.geekshop.entity.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-01
 */
@Data
public class CategoryTree {
    private final Category category;
    private final List<CategoryTree> childCategory;

    public CategoryTree(Category mainCategory) {
        this.category = mainCategory;
        childCategory = new ArrayList<>();
    }

    public void addChildren(CategoryTree category) {
        childCategory.add(category);
    }
}
