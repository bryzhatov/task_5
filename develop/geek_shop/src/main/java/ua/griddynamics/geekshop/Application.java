package ua.griddynamics.geekshop;

import ua.griddynamics.geekshop.controllers.CategoryController;
import ua.griddynamics.geekshop.controllers.PageController;
import ua.griddynamics.geekshop.controllers.ResourceController;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;

import java.io.IOException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class Application {
    public static void main(String[] args) throws IOException {
        HttpServerConfig config = new HttpServerConfig();
        config.setPort(8081);

        RepositoryFacade facade = new RepositoryFacade();

        HttpServer httpServer = new HttpServer(config);

        mappingResourceController(httpServer);
        mappingPageController(httpServer, facade);
        mappingCategoryController(httpServer, facade);

        httpServer.deploy();
    }

    private static void mappingPageController(HttpServer server, RepositoryFacade facade) {
        PageController pageController = new PageController(
                new FreemarkerTemplate("/web"),
                facade);

        server.addReaction("/", "GET", pageController::getIndex);
    }

    private static void mappingResourceController(HttpServer server) {
        ResourceController resourceController = new ResourceController();

        server.addReaction("/in.css", "GET", resourceController::getCss);
    }

    private static void mappingCategoryController(HttpServer server, RepositoryFacade facade) {
        CategoryController categoryController = new CategoryController(facade);

        server.addReaction("/v1/categories", "GET", categoryController::getCategories);
        server.addReaction("/v1/categories/main", "GET", categoryController::getMainCategories);
    }
}
