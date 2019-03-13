package ua.griddynamics.geekshop.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-13
 */
@Data
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.NONE)
    private int id;
    private String name;
    private String firstName;
}
