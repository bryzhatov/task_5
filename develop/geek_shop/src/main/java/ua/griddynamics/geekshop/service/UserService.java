package ua.griddynamics.geekshop.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.griddynamics.geekshop.entity.User;
import ua.griddynamics.geekshop.repository.api.UserRepository;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-15
 */
@Data
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User get(int id) {
        return userRepository.get(id);
    }

    public User get(String login, String password) {
        return userRepository.get(login, password);
    }
}
