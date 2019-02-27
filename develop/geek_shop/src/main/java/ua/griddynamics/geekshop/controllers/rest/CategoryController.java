package ua.griddynamics.geekshop.controllers.rest;

import com.google.gson.Gson;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.exception.AppException;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
@Log4j
public class CategoryController {
    @Setter
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void getCategories(HttpRequest request, HttpResponse response) {
        Gson gson = new Gson();
        try {

            String jsonCategory = gson.toJson(categoryService.getCategories());
            response.write(jsonCategory);

        } catch (AppException e) {
            log.error(e);
            response.setStatus(500);
        }
    }

    public void getMainCategories(HttpRequest request, HttpResponse response) {
        Gson gson = new Gson();
        try {

            String jsonCategory = gson.toJson(categoryService.getMainCategories());
            response.write(jsonCategory);

        } catch (ServiceException e) {
            log.error(e);
            response.setStatus(500);
        }
    }
}
