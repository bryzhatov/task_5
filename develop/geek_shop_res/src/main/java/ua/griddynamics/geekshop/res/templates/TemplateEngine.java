package ua.griddynamics.geekshop.res.templates;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface TemplateEngine {
    String render( String fileTemplate, Object pageValue);
}
