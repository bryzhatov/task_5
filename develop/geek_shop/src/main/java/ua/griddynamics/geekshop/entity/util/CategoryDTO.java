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
public class CategoryDTO {
    private final Category category;
    private final List<CategoryDTO> childCategory;

    public CategoryDTO(Category mainCategory) {
        this.category = mainCategory;
        childCategory = new ArrayList<>();
    }

    public void addChildren(CategoryDTO category) {
        childCategory.add(category);
    }
}
