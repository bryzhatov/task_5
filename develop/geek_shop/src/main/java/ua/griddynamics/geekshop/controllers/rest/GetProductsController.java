package ua.griddynamics.geekshop.controllers.rest;

import com.google.gson.Gson;
import lombok.Setter;
import ua.griddynamics.geekshop.service.ProductService;
import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;
import ua.griddynamics.httpserver.api.Reaction;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public class GetProductsController implements Reaction {

    @Setter
    private ProductService productService;

    public GetProductsController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void react(HttpRequest request, HttpResponse response) throws Exception {
        response.write(new Gson().toJson(productService.getAllProducts()));
    }
}
