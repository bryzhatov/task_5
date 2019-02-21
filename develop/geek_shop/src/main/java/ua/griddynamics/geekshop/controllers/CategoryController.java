package ua.griddynamics.geekshop.controllers;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.geekshop.service.facade.ServiceFacade;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
@Log4j
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(ServiceFacade serviceFacade) {
        categoryService = serviceFacade.getCategoryService();
    }

    public void getCategories(HttpRequest request, HttpResponse response) {
        Gson gson = new Gson();
        try {
            String jsonCategory = gson.toJson(categoryService.getCategories());
            response.getWriter().write(jsonCategory);

        } catch (ServiceException e) {
            log.error(e);
        }
    }

    public void getMainCategories(HttpRequest request, HttpResponse response) {
        Gson gson = new Gson();
        try {
            String jsonCategory = gson.toJson(categoryService.getMainCategories());
            response.getWriter().write(jsonCategory);

        } catch (ServiceException e) {
            log.error(e);
        }
    }
}
