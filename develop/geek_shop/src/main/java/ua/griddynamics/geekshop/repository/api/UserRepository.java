package ua.griddynamics.geekshop.repository.api;

import ua.griddynamics.geekshop.entity.User;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-15
 */
public interface UserRepository {
    User get(int id);
    User get(String login, String password);
}
