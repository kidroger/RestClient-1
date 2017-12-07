package net.devfront.rest.exception;

/**
 * Base class for exceptions whenever it encounters errors.
 */
public class RestClientException extends Exception {
    /**
     * Construct a new instance of {@code RestClientException} with the given message.
     * @param message the message
     */
    public RestClientException(String message) {
        super(message);
    }

    /**
     * Construct a new instance of {@code RestClientException} with the given message and exception.
     * @param message the message
     * @param throwable the exception
     */
    public RestClientException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
