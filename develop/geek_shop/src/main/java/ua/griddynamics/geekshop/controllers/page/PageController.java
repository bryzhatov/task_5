package ua.griddynamics.geekshop.controllers.page;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.controllers.entity.Model;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.session.api.SessionService;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
@Data
public class PageController {
    private TemplateEngine templateEngine;
    private CategoryService categoryService;
    private SessionService sessionService;

    public PageController(CategoryService categoryService, TemplateEngine templateEngine,
                          SessionService sessionService) {
        this.categoryService = categoryService;
        this.templateEngine = templateEngine;
        this.sessionService = sessionService;
    }

    public String getIndex(HttpRequest request, HttpResponse response, Model model) {
        response.addHeader("Content-Type", "text/html");
        return "/pages/index.html";
    }

    public String getCategory(HttpRequest request, HttpResponse response, Model model) {
        String idParam = request.getParameter("id");
        int id = 0;

        if (idParam != null) {
            id = Integer.valueOf(idParam);
        }

        model.add("category", categoryService.get(id));
        return "/pages/category.html";
    }

    public String getProduct(HttpRequest request, HttpResponse response, Model model) {
        String idParam = request.getParameter("id");
        int id = 0;

        if (idParam != null) {
            id = Integer.valueOf(idParam);
        }

        model.add("id", id);
        return "/pages/product_info.html";
    }

    public String getLogin(HttpRequest request, HttpResponse response, Model model) {
        return "/pages/login.html";
    }
}
