package ua.griddynamics.geekshop;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.controllers.CategoryController;
import ua.griddynamics.geekshop.controllers.PageController;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnection;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.geekshop.service.facade.ServiceFacade;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class Application {

    public static void main(String[] args) throws IOException {
//        Properties properties = getProperties("app/geek_shop_db.properties");
//        GeekShopConnection geekShopConnection = new GeekShopConnection(properties);
//        ServiceFacade serviceFacade = new ServiceFacade(new RepositoryFacade(geekShopConnection));

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

    private static Properties getProperties(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(name)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            log.error("Can't load DB properties: " + e);
        }
        return properties;
    }
}
