package ua.griddynamics.geekshop.controllers.rest;

import com.google.gson.Gson;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.api.Reaction;

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
        int deep = Integer.valueOf(request.getParameter("deep"));

        if (deep > 0) {
            response.write(new Gson().toJson(categoryService.getCategories(deep)));
        } else {
            response.setStatus(400);
        }
    }
}
