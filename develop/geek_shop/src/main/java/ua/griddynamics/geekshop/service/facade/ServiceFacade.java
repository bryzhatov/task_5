package ua.griddynamics.geekshop.service.facade;

import lombok.Getter;
import ua.griddynamics.geekshop.repository.RepositoryFacade;
import ua.griddynamics.geekshop.service.CategoryService;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
public class ServiceFacade {
    @Getter
    private final CategoryService categoryService;

    public ServiceFacade(RepositoryFacade repositoryFacade) {
        this.categoryService = new CategoryService(repositoryFacade.getCategoryPostgresRepository());
    }
}
