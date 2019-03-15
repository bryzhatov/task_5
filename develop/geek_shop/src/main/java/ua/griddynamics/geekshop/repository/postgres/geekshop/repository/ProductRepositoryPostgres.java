package ua.griddynamics.geekshop.repository.postgres.geekshop.repository;

import ua.griddynamics.geekshop.entity.Product;
import ua.griddynamics.geekshop.exception.DataBaseException;
import ua.griddynamics.geekshop.repository.api.ProductRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
public class ProductRepositoryPostgres implements ProductRepository {
    private final Supplier<Connection> connectionSupplier;

    public ProductRepositoryPostgres(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public List<Product> get(int rating, int count) throws DataBaseException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = connectionSupplier.get()) {

            try (PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM products WHERE rating > ? LIMIT ?")) {
                statement.setInt(1, rating);
                statement.setInt(2, count);

                try(ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()) {
                        Product product = productMapper(resultSet);
                        products.add(product);
                    }
                }

                return products;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> get(List<String> conditions) {
        String sqlconditionsBuilder;
        return null;
    }

    @Override
    public Product get(int id) {
        try (Connection connection = connectionSupplier.get()) {

            try (PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM products WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    return productMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    @Override
    public void add(Product product) {
        try (Connection connection = connectionSupplier.get()) {

            try (PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO products(id, count, name, category_id, description, manufacturer_id, price, image_link) " +
                            "VALUES (?,?,?,?,?,?,?,?)")) {
                int i = 0;
                preparedStatement.setInt(++i, product.getId());
                preparedStatement.setLong(++i, product.getCount());
                preparedStatement.setString(++i, product.getName());
                preparedStatement.setLong(++i, product.getCategoryId());
                preparedStatement.setString(++i, product.getDescription());
                preparedStatement.setLong(++i, product.getManufacturerId());
                preparedStatement.setLong(++i, product.getPrice().longValue());
                preparedStatement.setString(++i, product.getImageLink());

                preparedStatement.execute();

            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        }
    }

    private Product productMapper(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        long count = resultSet.getLong("count");
        String name = resultSet.getString("name");
        long categoryId = resultSet.getLong("category_id");
        String description = resultSet.getString("description");
        long manufacturerId = resultSet.getLong("manufacturer_id");
        long price = resultSet.getLong("price");
        String imageLink = resultSet.getString("image_link");

        return new Product(
                id,
                count,
                name,
                categoryId,
                imageLink,
                new BigDecimal(new BigInteger(Long.toString(price)), 2),
                description,
                manufacturerId);
    }
}
