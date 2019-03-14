package ua.griddynamics.geekshop;

import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j;
import org.yaml.snakeyaml.Yaml;
import ua.griddynamics.geekshop.controllers.AuthController;
import ua.griddynamics.geekshop.controllers.entity.GeekReaction;
import ua.griddynamics.geekshop.controllers.entity.Model;
import ua.griddynamics.geekshop.controllers.page.PageController;
import ua.griddynamics.geekshop.controllers.rest.CategoryRestController;
import ua.griddynamics.geekshop.controllers.rest.ProductRestController;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.repository.api.ProductRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnectionProvider;
import ua.griddynamics.geekshop.repository.postgres.geekshop.repository.CategoryPostgresRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.repository.ProductPostgresRepository;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.res.templates.ftl.FreemarkerTemplate;
import ua.griddynamics.geekshop.service.CategoryService;
import ua.griddynamics.geekshop.service.ProductService;
import ua.griddynamics.geekshop.util.config.AntonConfigAdapter;
import ua.griddynamics.geekshop.util.json.converter.JsonConverter;
import ua.griddynamics.geekshop.util.json.factory.JsonConverterFactory;
import ua.griddynamics.httpserver.HttpServer;
import ua.griddynamics.httpserver.api.controller.RequestMethods;
import ua.griddynamics.httpserver.session.HashMapSessionManager;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionManager;
import ua.griddynamics.httpserver.utils.controllers.StaticControllerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

import static ua.griddynamics.httpserver.api.controller.RequestMethods.GET;
import static ua.griddynamics.httpserver.api.controller.RequestMethods.POST;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Log4j
public class Application {
    private static TemplateEngine templateEngine = new FreemarkerTemplate("/web");
    private static SessionManager sessionManager = new HashMapSessionManager();

    public static void main(String[] args) throws IOException, URISyntaxException {
        Properties properties = getProperties();

        // Configs, Connections
        HttpServer httpServer = new HttpServer(AntonConfigAdapter.getConfig(properties));
        GeekShopConnectionProvider geekShopConnectionProvider = new GeekShopConnectionProvider(properties);
        JsonConverter jsonConverter = JsonConverterFactory.create("gson");

        // Repositories
        CategoryRepository categoryRepository = new CategoryPostgresRepository(geekShopConnectionProvider);
        ProductRepository productRepository = new ProductPostgresRepository(geekShopConnectionProvider);

        // Services
        CategoryService categoryService = new CategoryService(categoryRepository);
        ProductService productService = new ProductService(productRepository);

        // Controllers:
        CategoryRestController categoryRestController = new CategoryRestController(categoryService, jsonConverter);
        ProductRestController getProductsController = new ProductRestController(productService, jsonConverter);
        PageController pageController = new PageController(categoryService, templateEngine, sessionManager);
        AuthController authController = new AuthController(sessionManager);

        // Reactions: Util
        addReaction(httpServer, "/auth", POST, authController::auth);
        addReaction(httpServer, "/login", GET, pageController::getLogin);

        // Reactions: Page
        addReaction(httpServer, "/", GET, pageController::getIndex);

        addReaction(httpServer, "/category/", GET, pageController::getCategory);
        addReaction(httpServer, "/product/", GET, pageController::getProduct);

        // Reactions: REST
        httpServer.addReaction("/v1/category/", GET, categoryRestController::getCategory);
        httpServer.addReaction("/v1/categories/", GET, categoryRestController::getCategories);

        httpServer.addReaction("/v1/products/", GET, getProductsController::getProductsByRating);
        httpServer.addReaction("/v1/product/", GET, getProductsController::getProduct);
        httpServer.addReaction("/static/*", GET, StaticControllerFactory.classpath("/web/static/"));

        httpServer.deploy();
    }

    private static void addReaction(HttpServer server, String url, RequestMethods method, GeekReaction reaction) {
        Model model = new Model();
        server.addReaction(url, method, (request, response) -> {

            String view = reaction.react(request, response, model);
            try {

                Session session = sessionManager.get(request.getCookie("sessionId"));
                if (session != null) {
                    model.add("session", session);
                }

                response.write(templateEngine.render(view, model));
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Properties getProperties() {
        Properties properties = new Properties();

        try (InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("app/app.yml")) {
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
                    convertMapToProperties((Map<String, Object>) entry.getValue(), properties, domainName.equals("") ? entry.getKey() : domainName + "." + entry.getKey());
                } else {
                    properties.setProperty(domainName + "." + entry.getKey(), entry.getValue().toString());
                }
            }
        }
    }
}
