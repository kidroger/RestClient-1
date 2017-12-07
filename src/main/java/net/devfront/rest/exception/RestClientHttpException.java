package net.devfront.rest.exception;

/**
 * Exception thrown when there is an HTTP error(4xx or 5xx).
 */
public class RestClientHttpException extends RestClientException {
    private final int statusCode;
    private final String statusText;

    /**
     * Construct a new instance of with the given parameters.
     *
     * @param message the message
     * @param statusCode the status code
     * @param statusText the status text
     */
    public RestClientHttpException(String message, int statusCode, String statusText) {
        super(message);
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}
