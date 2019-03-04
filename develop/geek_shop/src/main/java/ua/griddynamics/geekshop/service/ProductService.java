package ua.griddynamics.geekshop.service;

import lombok.Setter;
import ua.griddynamics.geekshop.entity.Product;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.exception.ServiceException;
import ua.griddynamics.geekshop.repository.api.ProductRepository;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public class ProductService {

    @Setter
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() throws ServiceException {
        try {
            return productRepository.getAllProducts();
        } catch (DataBaseException e) {
            throw new ServiceException("Can't get all categories", e);
        }
    }
}
