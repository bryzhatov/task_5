package ua.griddynamics.geekshop;

import ua.griddynamics.geekshop.controllers.CategoryController;
import ua.griddynamics.geekshop.controllers.PageController;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnection;
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
//        GeekShopConnection geekShopConnection = new GeekShopConnection("app/geek_shop_db.properties");
//        ServiceFacade serviceFacade = new ServiceFacade(new RepositoryFacade(geekShopConnection));
//
//        PageController pageController = new PageController(serviceFacade,
//                new FreemarkerTemplate("/web"));
//        CategoryController categoryController = new CategoryController(serviceFacade);

        HttpServerConfig config = new HttpServerConfig()
                .setPort(8081)
                .setVisibleRequest(true);

        HttpServer httpServer = new HttpServer(config);
        httpServer.addReaction("/", "GET", ((request, response) -> response.getWriter().write("lol kek cheburek")));
//        httpServer.addReaction("/", "GET", pageController::getIndex);
//        httpServer.addReaction("/v1/categories", "GET", categoryController::getCategories);
//        httpServer.addReaction("/v1/categories/main", "GET", categoryController::getMainCategories);

        httpServer.deploy();
    }
}
