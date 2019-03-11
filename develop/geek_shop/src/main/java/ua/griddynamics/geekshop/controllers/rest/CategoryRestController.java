package ua.griddynamics.geekshop.controllers.rest;

import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.geekshop.util.json.converter.JsonConverter;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-28
 */
public class CategoryRestController {
    private CategoryService categoryService;
    private JsonConverter jsonConverter;

    public CategoryRestController(CategoryService categoryService, JsonConverter jsonConverter) {
        this.categoryService = categoryService;
        this.jsonConverter = jsonConverter;
    }

    public void getCategory(HttpRequest request, HttpResponse response) {
        int id = Integer.valueOf(request.getParameter("id"));
        response.write(jsonConverter.toJson(categoryService.get(id)));
    }

    public void getCategories(HttpRequest request, HttpResponse response) {
        String deepStr = request.getParameter("deep");
        String categoryIdSrt = request.getParameter("categoryId");

        int deep = deepStr != null ? Integer.valueOf(deepStr) : 0;
        int categoryId = categoryIdSrt != null ? Integer.valueOf(categoryIdSrt) : 0;

        response.write(jsonConverter.toJson(categoryService.get(deep, categoryId)));
    }
}
