package ua.griddynamics.geekshop.repository.api;

import ua.griddynamics.geekshop.entity.Product;
import ua.griddynamics.geekshop.exception.DataBaseException;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public interface ProductRepository {
    List<Product> get(int rating, int count);
    Product get(int id);
    void add(Product product);
}
