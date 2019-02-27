package ua.griddynamics.geekshop;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.controllers.PageController;
import ua.griddynamics.geekshop.controllers.rest.CategoryController;
import ua.griddynamics.geekshop.repository.postgres.geekshop.CategoryPostgresRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnectionProvider;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;
import ua.griddynamics.httpserver.utils.controllers.StaticControllerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import static ua.griddynamics.httpserver.api.controller.RequestMethods.GET;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class Application {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Properties properties = getProperties("app/app.properties");

        GeekShopConnectionProvider geekShopConnectionProvider = new GeekShopConnectionProvider(properties);
        CategoryPostgresRepository categoryPostgresRepository = new CategoryPostgresRepository(geekShopConnectionProvider);
        CategoryService categoryService = new CategoryService(categoryPostgresRepository);

        PageController pageController = new PageController(categoryService,
                new FreemarkerTemplate("/web"));

        CategoryController categoryController = new CategoryController(categoryService);

        HttpServerConfig config = new HttpServerConfig()
                .setPort(8080);

        HttpServer httpServer = new HttpServer(config);
        httpServer.addReaction("/", GET, pageController::getIndex);
        httpServer.addReaction("/v1/categories", GET, categoryController::getCategories);
        httpServer.addReaction("/v1/categories/main", GET, categoryController::getMainCategories);
        httpServer.addReaction("/static/*", GET, StaticControllerFactory.classpath("/web/static")::getResources);
        httpServer.addReaction("/static/at/*", GET, StaticControllerFactory.classpath("/other/static")::getResources);
        httpServer.deploy();
    }

    private static Properties getProperties(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(name)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            //TODO fatal
            log.fatal("Can't load DB properties", e);
        }
        return properties;
    }
}
