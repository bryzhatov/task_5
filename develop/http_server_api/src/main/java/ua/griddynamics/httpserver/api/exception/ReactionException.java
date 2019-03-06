package ua.griddynamics.httpserver.api.exception;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-06
 */
public class ReactionException extends Exception {
    public ReactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
