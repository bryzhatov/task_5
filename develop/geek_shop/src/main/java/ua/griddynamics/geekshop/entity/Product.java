package ua.griddynamics.geekshop.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-04
 */
@Data
@AllArgsConstructor
public class Product {
    @Setter(AccessLevel.NONE)
    private int id;
    private long count;
    private String name;
    private long categoryId;
    private BigDecimal price;
    private String description;
    private long manufacturerId;
}
