package ua.griddynamics.geekshop.controllers.page;

import com.google.gson.Gson;
import freemarker.template.TemplateException;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.entity.Category;
import ua.griddynamics.geekshop.entity.CategoryDTO;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.api.Reaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class GetMenuPageController implements Reaction {
    private final TemplateEngine templateEngine;
    @Setter
    private CategoryService categoryService;

    public GetMenuPageController(CategoryService categoryService, TemplateEngine engine) {
        this.categoryService = categoryService;
        this.templateEngine =engine;
    }

    @Override
    public void react(HttpRequest request, HttpResponse response) {
        try {
            String page = templateEngine.render("menu.html",
                    singletonMap("categories", categoryService.getCategories())
            );
            response.write(page);
        } catch (TemplateException | ServiceException e) {
            log.error(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
