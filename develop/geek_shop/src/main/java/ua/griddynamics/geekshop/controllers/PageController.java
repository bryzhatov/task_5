package ua.griddynamics.geekshop.controllers;

import freemarker.template.TemplateException;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.geekshop.service.facade.ServiceFacade;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

import static java.util.Collections.singletonMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class PageController {
    @Setter
    private CategoryService categoryService;
    private final TemplateEngine templateEngine;

    public PageController(ServiceFacade serviceFacade, TemplateEngine templateEngine) {
        categoryService = serviceFacade.getCategoryService();
        this.templateEngine = templateEngine;
    }

    public void getIndex(HttpRequest request, HttpResponse response) {
        try {
            String page = templateEngine.render("index.html",
                    singletonMap("categories", categoryService.getCategories())
            );
            response.getWriter().write(page);
        } catch (TemplateException | ServiceException e) {
            log.error(e);
        }
    }
}
