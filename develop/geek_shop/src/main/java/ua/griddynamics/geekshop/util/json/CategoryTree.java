package ua.griddynamics.geekshop.util.json;

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
    private final int id;
    private final String name;
    private final int parentId;
    private final List<CategoryTree> childCategory;

    public CategoryTree(Category mainCategory) {
        this.id = mainCategory.getId();
        this.name = mainCategory.getName();
        this.parentId = mainCategory.getParentId();
        childCategory = new ArrayList<>();
    }

    public void addChildren(CategoryTree category) {
        childCategory.add(category);
    }
}
