package ua.griddynamics.geekshop;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.controllers.page.GetIndexPageController;
import ua.griddynamics.geekshop.controllers.rest.GetMainCategoriesController;
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
        Properties properties = getProperties();

        GeekShopConnectionProvider geekShopConnectionProvider = new GeekShopConnectionProvider(properties);
        CategoryPostgresRepository categoryPostgresRepository = new CategoryPostgresRepository(geekShopConnectionProvider);
        CategoryService categoryService = new CategoryService(categoryPostgresRepository);

        HttpServerConfig config = new HttpServerConfig(properties);

        HttpServer httpServer = new HttpServer(config);
        httpServer.addReaction("/", GET, new GetIndexPageController(categoryService, new FreemarkerTemplate("/web")));
        httpServer.addReaction("/v1/categories/main", GET, new GetMainCategoriesController(categoryService));
        httpServer.addReaction("/static/*", GET, StaticControllerFactory.classpath("/web/static"));
        httpServer.addReaction("/static/at/*", GET, StaticControllerFactory.classpath("/other/static"));
        httpServer.deploy();
    }

    private static Properties getProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("app/app.properties")) {
            properties.load(resourceStream);
        } catch (IOException e) {
            log.fatal("Can't load DB properties", e);
        }
        return properties;
    }
}
