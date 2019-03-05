package ua.griddynamics.geekshop.controllers.rest;

import com.google.gson.Gson;
import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.entity.util.CategoryDTO;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.api.Reaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-28
 */
public class GetCategoriesController implements Reaction {
    private CategoryService categoryService;

    public GetCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void react(HttpRequest request, HttpResponse response) {
        List<CategoryDTO> answer = new ArrayList<>();
        Map<Integer, CategoryDTO> categoryDTOMap = new HashMap<>();

        for (Category category : categoryService.getCategories()) {
            categoryDTOMap.put(category.getId(), new CategoryDTO(category));
        }

        for (Map.Entry<Integer, CategoryDTO> entry : categoryDTOMap.entrySet()) {
            int parentId = entry.getValue().getCategory().getParentId();

            if (parentId != 0) {
                CategoryDTO parentCategory = categoryDTOMap.get(parentId);
                parentCategory.addChildren(entry.getValue());
            } else {
                answer.add(entry.getValue());
            }
        }

        response.write(new Gson().toJson(answer));
    }
}
