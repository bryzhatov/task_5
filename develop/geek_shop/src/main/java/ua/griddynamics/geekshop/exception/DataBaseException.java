package ua.griddynamics.geekshop.exception;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class DataBaseException extends RuntimeException {
    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
