package ua.griddynamics.geekshop.repository;

import ua.griddynamics.geekshop.repository.api.CategoryRepository;
import ua.griddynamics.geekshop.repository.postgres.PostgresConnection;
import ua.griddynamics.geekshop.repository.postgres.geekshop.CategoryPostgresRepository;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnection;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class RepositoryFacade {
    private final PostgresConnection postgresConnection = new GeekShopConnection();
    private final CategoryPostgresRepository categoryPostgresRepository
            = new CategoryPostgresRepository(postgresConnection);

    public CategoryRepository getCategoryPostgres(){
        return categoryPostgresRepository;
    }
}
