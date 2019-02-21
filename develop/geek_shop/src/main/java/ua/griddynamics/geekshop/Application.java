package ua.griddynamics.geekshop;

import ua.griddynamics.geekshop.controllers.CategoryController;
import ua.griddynamics.geekshop.controllers.PageController;
import ua.griddynamics.geekshop.controllers.ResourceController;
import ua.griddynamics.geekshop.repository.ConnectionFacade;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.geekshop.service.facade.ServiceFacade;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;

import java.io.IOException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class Application {

    public static void main(String[] args) throws IOException {
        ConnectionFacade connectionFacade = new ConnectionFacade();
        RepositoryFacade repositoryFacade = new RepositoryFacade(connectionFacade);
        ServiceFacade serviceFacade = new ServiceFacade(repositoryFacade);

        PageController pageController = new PageController(serviceFacade,
                new FreemarkerTemplate("/web"));
        CategoryController categoryController = new CategoryController(serviceFacade);

        HttpServerConfig config = new HttpServerConfig();
        config.setPort(8081);

        HttpServer httpServer = new HttpServer(config);
        httpServer.addReaction("/", "GET", pageController::getIndex);
        httpServer.addReaction("/v1/categories", "GET", categoryController::getCategories);
        httpServer.addReaction("/v1/categories/main", "GET", categoryController::getMainCategories);

        httpServer.deploy();
    }
}
