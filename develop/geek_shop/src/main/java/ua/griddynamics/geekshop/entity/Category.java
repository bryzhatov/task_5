package ua.griddynamics.geekshop.entity;

import lombok.*;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Data
@AllArgsConstructor
public class Category {
    @Setter(AccessLevel.NONE)
    private int id;
    private String name;
    private int parentId;
}
