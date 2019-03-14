package ua.griddynamics.geekshop.controllers.page;

import freemarker.template.TemplateException;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
@Data
public class PageController {
    private TemplateEngine templateEngine;
    private CategoryService categoryService;
    private SessionManager sessionManager;

    public PageController(CategoryService categoryService,
                          TemplateEngine templateEngine,
                          SessionManager sessionManager) {
        this.categoryService = categoryService;
        this.templateEngine = templateEngine;
        this.sessionManager = sessionManager;
    }

    public void getIndex(HttpRequest request, HttpResponse response) {
        try {
            Map<String, Object> model = new HashMap<>();

            if (request.getCookie("sessionId") != null) {
                Session session = sessionManager.get(request.getCookie("sessionId"));
                model.put("session", session);
            }

            response.addHeader("Content-Type", "text/html");

            String page = templateEngine.render("/pages/index.html", model);
            response.write(page);
        } catch (TemplateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void getCategory(HttpRequest request, HttpResponse response) {
        Map<String, String> model = new HashMap<>();

        String idParam = request.getParameter("id");
        int id = 0;

        if (idParam != null) {
            id = Integer.valueOf(idParam);
        }

        try {
            response.write(templateEngine.render("/pages/category.html", categoryService.get(id)));
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void getProduct(HttpRequest request, HttpResponse response) {
        String idParam = request.getParameter("id");
        int id = 0;

        if (idParam != null) {
            id = Integer.valueOf(idParam);
        }

        try {
            response.write(templateEngine.render(
                    "/pages/product_info.html",
                    new Category(id, "", 0)));
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void getLogin(HttpRequest request, HttpResponse response) {
        try {
            response.write(templateEngine.render(
                    "/pages/login.html"));
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
