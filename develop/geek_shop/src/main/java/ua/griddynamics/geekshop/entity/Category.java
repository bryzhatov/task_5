package ua.griddynamics.geekshop.entity;

import lombok.Data;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
@Data
public class Category {
    private int parentId;
    private String name;
    private int id;
}
