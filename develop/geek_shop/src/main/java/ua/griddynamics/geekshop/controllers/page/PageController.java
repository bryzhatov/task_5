package ua.griddynamics.geekshop.controllers.page;

import freemarker.template.TemplateException;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class PageController {
    private final TemplateEngine templateEngine;
    @Setter
    private CategoryService categoryService;

    public PageController(CategoryService categoryService, TemplateEngine templateEngine) {
        this.categoryService = categoryService;
        this.templateEngine = templateEngine;
    }

    public void getIndex(HttpRequest request, HttpResponse response) {
        try {
            response.addHeader("Content-Type", "text/html");

            String page = templateEngine.render("index.html");
            response.write(page);

        } catch (TemplateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void getCategory(HttpRequest request, HttpResponse response) {
        String idParam = request.getParameter("id");
        int id = 0;

        if (idParam != null) {
            id = Integer.valueOf(idParam);
        }

        if (id > 0) {
            try {
                response.write(templateEngine.render("/pages/category.html", categoryService.getCategory(id)));
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.setStatus(400);
        }
    }
}
