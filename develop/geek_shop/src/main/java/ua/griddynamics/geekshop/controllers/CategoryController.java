package ua.griddynamics.geekshop.controllers;

import com.google.gson.Gson;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(RepositoryFacade repositoryFacade) {
        categoryService = new CategoryService(repositoryFacade);
    }

    public void getCategories(HttpRequest request, HttpResponse response) {
        Gson gson = new Gson();
        try {
            String jsonCategory = gson.toJson(categoryService.getCategories());
            response.getWriter().write(jsonCategory);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void getMainCategories(HttpRequest request, HttpResponse response) {
        Gson gson = new Gson();
        try {
            String jsonCategory = gson.toJson(categoryService.getMainCategories());
            response.getWriter().write(jsonCategory);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
