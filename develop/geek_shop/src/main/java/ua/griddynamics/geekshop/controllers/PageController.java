package ua.griddynamics.geekshop.controllers;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.res.templates.ftl.utils.FreeMarkers;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class PageController {
    private final CategoryService categoryService;
    private final TemplateEngine templateEngine;

    public PageController(TemplateEngine templateEngine, RepositoryFacade repositoryFacade) {
        categoryService = new CategoryService(repositoryFacade);
        this.templateEngine = templateEngine;
    }

    public void getIndex(HttpRequest request, HttpResponse response) {
        try {
            String page = templateEngine.render("index.ftl",
                    FreeMarkers.referByName("categories", categoryService.getCategories()));

            response.getWriter().write(page);
        } catch (ServiceException e) {
            log.error(e);
        }
    }
}
