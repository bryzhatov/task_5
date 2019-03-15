package ua.griddynamics.httpserver.session.api;

import lombok.Data;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-15
 */
@Data
public class Meta {
    private String className;
    private Object object;

    public Meta(String className, Object object) {
        this.className = className;
        this.object = object;
    }
}
