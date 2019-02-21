package ua.griddynamics.geekshop.repository;

import lombok.Getter;
import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.CategoryPostgresRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnection;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class RepositoryFacade {
    @Getter
    private final CategoryRepository categoryPostgresRepository;

    public RepositoryFacade(ConnectionFacade facade) {
        GeekShopConnection geekShopConnection = facade.getGeekShopConnection();
        categoryPostgresRepository = new CategoryPostgresRepository(geekShopConnection::getConnection);
    }
}
