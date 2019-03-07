package ua.griddynamics.geekshop.res.templates;

import freemarker.template.TemplateException;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public interface TemplateEngine {
    String render(String fileTemplate) throws TemplateException;
    String render(String fileTemplate, Object pageValue) throws TemplateException;
}
