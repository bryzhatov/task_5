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
        int id = Integer.valueOf(request.getParameter("id"));
        response.write(converter.toJson(productService.get(id)));
    }

    public void getProductsByRating(HttpRequest request, HttpResponse response) {
        String ratingStr = request.getParameter("rating");
        String countStr = request.getParameter("count");
        String categoryIdStr = request.getParameter("categoryId");

        int rating = ratingStr != null ? Integer.valueOf(ratingStr) : 0;
        int count = countStr != null ? Integer.valueOf(countStr) : 0;
        int categoryId = categoryIdStr != null ? Integer.valueOf(categoryIdStr) : 0;

        response.write(converter.toJson(productService.get(rating, count)));
    }
}
