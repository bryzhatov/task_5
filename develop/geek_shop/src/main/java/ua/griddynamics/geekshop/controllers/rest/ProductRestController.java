package ua.griddynamics.geekshop.controllers.rest;

import lombok.Setter;
import ua.griddynamics.geekshop.service.ProductService;
import ua.griddynamics.geekshop.util.json.converter.JsonConverter;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public class ProductRestController {
    @Setter
    private JsonConverter converter;
    @Setter
    private ProductService productService;

    public ProductRestController(ProductService productService, JsonConverter converter) {
        this.productService = productService;
        this.converter = converter;
    }

    public void getProduct(HttpRequest request, HttpResponse response) {
        String idParam = request.getParameter("id");
        int id = 0;

        if (idParam != null) {
            id = Integer.valueOf(idParam);
        }

        if (id > 0) {
            response.write(converter.toJson(productService.get(id)));
        } else {
            response.setStatus(400);
        }
    }

    public void getAllProducts(HttpRequest request, HttpResponse response) {
        response.write(converter.toJson(productService.getAll()));
    }
}
