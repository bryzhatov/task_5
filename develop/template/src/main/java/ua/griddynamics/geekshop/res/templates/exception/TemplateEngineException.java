package ua.griddynamics.geekshop.res.templates.exception;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
public class TemplateEngineException extends RuntimeException {
    public TemplateEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
