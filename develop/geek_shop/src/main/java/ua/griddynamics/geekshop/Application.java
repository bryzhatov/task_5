package ua.griddynamics.geekshop;

import lombok.extern.log4j.Log4j;
import org.yaml.snakeyaml.Yaml;
import ua.griddynamics.geekshop.controllers.page.PageController;
import ua.griddynamics.geekshop.controllers.rest.CategoryRestController;
import ua.griddynamics.geekshop.controllers.rest.ProductsRestController;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.repository.api.ProductRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnectionProvider;
import ua.griddynamics.geekshop.repository.postgres.geekshop.repository.CategoryPostgresRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.repository.ProductPostgresRepository;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.geekshop.service.ProductService;
import ua.griddynamics.geekshop.util.config.AntonConfigAdapter;
import ua.griddynamics.geekshop.util.json.factory.JsonConverterFactory;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.utils.controllers.StaticControllerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
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

        // Configs, Connections
        HttpServer httpServer = new HttpServer(AntonConfigAdapter.getConfig(properties));
        FreemarkerTemplate freemarkerTemplate = new FreemarkerTemplate("/web");
        GeekShopConnectionProvider geekShopConnectionProvider = new GeekShopConnectionProvider(properties);

        // Repositories
        CategoryRepository categoryRepository = new CategoryPostgresRepository(geekShopConnectionProvider);
        ProductRepository productRepository = new ProductPostgresRepository(geekShopConnectionProvider);

        // Services
        CategoryService categoryService = new CategoryService(categoryRepository);
        ProductService productService = new ProductService(productRepository);

        // Controllers: REST
        CategoryRestController categoryRestController = new CategoryRestController(categoryService, JsonConverterFactory.create("gson"));
        ProductsRestController getProductsController = new ProductsRestController(productService);

        // Controllers: Page
        PageController pageController = new PageController(categoryService, freemarkerTemplate);

        // Reactions: Page
        httpServer.addReaction("/", GET, pageController::getIndex);
        httpServer.addReaction("/category/", GET, pageController::getCategory);

        // Reactions: REST
        httpServer.addReaction("/v1/category/", GET, categoryRestController::getCategory);
        httpServer.addReaction("/v1/categories/", GET, categoryRestController::getCategories);
        httpServer.addReaction("/v1/products/", GET, getProductsController::getAllProducts);
        httpServer.addReaction("/static/*", GET, StaticControllerFactory.classpath("/web/static/"));

        httpServer.deploy();
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("app/app.yml")) {
            Map<String, Object> map = new Yaml().load(resourceStream);

            convertMapToProperties(map, properties, "");

        } catch (IOException e) {
            // TODO жопа
            log.fatal("Can't load DB properties", e);
        }
        return properties;
    }

    private static void convertMapToProperties(Map<String, Object> map, Properties properties, String domainName) {
        if (map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    convertMapToProperties((Map<String, Object>) entry.getValue(), properties, domainName + "." + entry.getKey());
                } else {
                    properties.setProperty(domainName + "." + entry.getKey(), entry.getValue().toString());
                }
            }
        }
    }
}
