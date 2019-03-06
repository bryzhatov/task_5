package ua.griddynamics.geekshop.exception;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-27
 */
public class AppException extends RuntimeException {
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
