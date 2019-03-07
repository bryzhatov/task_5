package ua.griddynamics.geekshop.controllers.rest;

import ua.griddynamics.geekshop.util.json.converter.JsonConverter;
import ua.griddynamics.geekshop.service.CategoryService;
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

    public void getCategories(HttpRequest request, HttpResponse response) {
        String deepStr = request.getParameter("deep");
        String categoryIdSrt = request.getParameter("categoryId");

        int deep = 0;
        if (deepStr != null) {
            deep = Integer.valueOf(deepStr);
        }
        int categoryId = 0;
        if (categoryIdSrt != null) {
            categoryId = Integer.valueOf(categoryIdSrt);
        }

        if (deep > 0 && categoryId >= 0) {
            response.write(jsonConverter.toJson(categoryService.getCategories(deep, categoryId)));
        } else {
            response.setStatus(400);
        }
    }
}
