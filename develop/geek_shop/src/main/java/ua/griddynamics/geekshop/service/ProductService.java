package ua.griddynamics.geekshop.service;

import lombok.Setter;
import ua.griddynamics.geekshop.entity.Product;
import ua.griddynamics.geekshop.repository.api.ProductRepository;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public class ProductService {
    @Setter
    private ProductRepository productRep;

    public ProductService(ProductRepository productRep) {
        this.productRep = productRep;
    }

    public List<Product> get(int rating, int count) {
        return productRep.get(rating, count);
    }

    public Product get(int id) {
        return productRep.get(id);
    }

    public List<Product> get(List<String> conditions){
        return productRep.get(conditions);
    }

    public void add(Product product) {
        productRep.add(product);
    }
}
