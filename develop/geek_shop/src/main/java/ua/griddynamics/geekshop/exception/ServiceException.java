package ua.griddynamics.geekshop.exception;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
}
