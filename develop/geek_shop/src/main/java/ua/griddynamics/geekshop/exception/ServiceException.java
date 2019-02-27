package ua.griddynamics.geekshop.exception;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class ServiceException extends AppException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
