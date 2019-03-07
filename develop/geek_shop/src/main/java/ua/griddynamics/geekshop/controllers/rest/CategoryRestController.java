package ua.griddynamics.geekshop.controllers.rest;

import com.google.gson.Gson;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-28
 */
public class CategoryRestController {
    private CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
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
            response.write(new Gson().toJson(categoryService.getCategories(deep, categoryId)));
        } else {
            response.setStatus(400);
        }
    }
}
