package ua.griddynamics.geekshop.controllers.rest;

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
        int parentId = Integer.parseInt(request.getParameter("parent_id"));
        int deep = Integer.parseInt(request.getParameter("deep"));

        System.out.println(parentId);
        System.out.println(deep);
        categoryService.getCategories(1, 3);
    }
}
