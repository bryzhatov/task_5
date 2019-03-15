package ua.griddynamics.geekshop;

import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j;
import org.yaml.snakeyaml.Yaml;
import redis.clients.jedis.Jedis;
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
import ua.griddynamics.httpserver.session.HashMapSessionService;
import ua.griddynamics.httpserver.session.api.Session;
import ua.griddynamics.httpserver.session.api.SessionRepository;
import ua.griddynamics.httpserver.session.api.SessionService;
import ua.griddynamics.httpserver.session.redis.repo.SessionRepositoryRedis;
import ua.griddynamics.httpserver.session.redis.service.SessionServiceRedis;
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
    // Configs
    private static GeekShopConnectionProvider geekShopConnectionProvider;
    private static TemplateEngine templateEngine;
    private static JsonConverter jsonConverter;
    private static Properties properties;
    private static HttpServer httpServer;
    // Repositories
    private static SessionRepository sessionRepositoryRedis;
    private static CategoryRepository categoryRepository;
    private static ProductRepository productRepository;
    // Services
    private static CategoryService categoryService;
    private static ProductService productService;
    private static SessionService sessionService;
    // Controllers
    private static CategoryRestController categoryRestController;
    private static ProductRestController productsRestController;
    private static PageController pageController;
    private static AuthController authController;


    public static void main(String[] args) throws IOException, URISyntaxException {
        initConfigs();
        initRepositories();
        initServices();
        initControllers();
        initRestControllers();

        httpServer.deploy();
    }

    private static void initConfigs() throws IOException {
        properties = getProperties();
        sessionService = new HashMapSessionService();
        jsonConverter = JsonConverterFactory.create("gson");
        templateEngine = new FreemarkerTemplate("/web");
        httpServer = new HttpServer(AntonConfigAdapter.getConfig(properties));
        geekShopConnectionProvider = new GeekShopConnectionProvider(properties);
    }

    private static void initRepositories() {
        productRepository = new ProductPostgresRepository(geekShopConnectionProvider);
        categoryRepository = new CategoryPostgresRepository(geekShopConnectionProvider);
//        sessionRepositoryRedis = new SessionRepositoryRedis(new Jedis("localhost"));
    }

    private static void initServices() {
        productService = new ProductService(productRepository);
        categoryService = new CategoryService(categoryRepository);
        sessionService = new HashMapSessionService();
    }

    private static void initControllers() {
        authController = new AuthController(sessionService);
        pageController = new PageController(categoryService, templateEngine, sessionService);

        addReaction(httpServer, "/", GET, pageController::getIndex);
        addReaction(httpServer, "/auth", POST, authController::auth);
        addReaction(httpServer, "/login", GET, pageController::getLogin);
        addReaction(httpServer, "/product/", GET, pageController::getProduct);
        addReaction(httpServer, "/category/", GET, pageController::getCategory);
    }

    private static void initRestControllers() throws URISyntaxException {
        productsRestController = new ProductRestController(productService, jsonConverter);
        categoryRestController = new CategoryRestController(categoryService, jsonConverter);

        httpServer.addReaction("/v1/product/", GET, productsRestController::getProduct);
        httpServer.addReaction("/v1/category/", GET, categoryRestController::getCategory);
        httpServer.addReaction("/v1/categories/", GET, categoryRestController::getCategories);
        httpServer.addReaction("/v1/products/", GET, productsRestController::getProductsByRating);
        httpServer.addReaction("/static/*", GET, StaticControllerFactory.classpath("/web/static/"));
    }

    private static void addReaction(HttpServer server, String url, RequestMethods method, GeekReaction reaction) {
        Model model = new Model();

        server.addReaction(url, method, (request, response) -> {

            String view = reaction.react(request, response, model);
            try {

                Session session = sessionService.get(request.getCookie("sessionId"));
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
