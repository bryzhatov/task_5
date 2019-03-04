package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.Product;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.ProductRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public class ProductPostgresRepository implements ProductRepository {
    private final Supplier<Connection> connectionSupplier;

    public ProductPostgresRepository(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public List<Product> getAllProducts() throws DataBaseException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = connectionSupplier.get()) {

            try (ResultSet resultSet = connection
                    .createStatement().executeQuery("SELECT * FROM \"products\"")) {

                if (resultSet != null) {
                    while (resultSet.next()) {
                        Product product = productMapper(resultSet);
                        products.add(product);
                    }
                    return products;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
        return products;
    }

    private Product productMapper(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        long count = resultSet.getLong("count");
        String name = resultSet.getString("name");
        long categoryId = resultSet.getLong("category_id");
        String description = resultSet.getString("description");
        long manufacturerId = resultSet.getLong("manufacturer_id");
        long price = resultSet.getLong("price");
        return new Product(id, count, name, categoryId, new BigDecimal(new BigInteger(Long.toString(price)), 2), description, manufacturerId);
    }
}
