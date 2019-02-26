package ua.griddynamics.geekshop;

import lombok.extern.log4j.Log4j;
import ua.griddynamics.geekshop.controllers.rest.CategoryController;
import ua.griddynamics.geekshop.controllers.PageController;
import ua.griddynamics.geekshop.repository.postgres.geekshop.CategoryPostgresRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnection;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.config.HttpServerConfig;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class Application {
    public static void main(String[] args) throws IOException {
        Properties properties = getProperties("app/geek_shop_db.properties");
        GeekShopConnection geekShopConnection = new GeekShopConnection(properties);
        CategoryPostgresRepository categoryPostgresRepository = new CategoryPostgresRepository(geekShopConnection::getConnection);
        CategoryService categoryService = new CategoryService(categoryPostgresRepository);

        PageController pageController = new PageController(categoryService,
                new FreemarkerTemplate("/web"));
        CategoryController categoryController = new CategoryController(categoryService);


        HttpServerConfig config = new HttpServerConfig()
                .setStaticFolder(Paths.get("/Users/dbryzhatov/Desktop/task_5/develop/geek_shop/src/main/resources/web/static"))
                .setPort(8080);

        HttpServer httpServer = new HttpServer(config);

        httpServer.addReaction("/", "GET", pageController::getIndex);
        httpServer.addReaction("/v1/categories", "GET", categoryController::getCategories);
        httpServer.addReaction("/v1/categories/main", "GET", categoryController::getMainCategories);

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
