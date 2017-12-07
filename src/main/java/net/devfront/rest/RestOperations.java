package net.devfront.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import net.devfront.rest.exception.RestClientException;
import org.apache.http.client.fluent.Request;

import java.util.List;

/**
 * Interface for REST operations.
 * Implemented by {@link RestClient}, it's a useful for testability, as it can easily be mocked or stubbed.
 */
public interface RestOperations {

    /**
     * Retrieve an object of responseType by sending a {@link org.apache.http.client.fluent.Request}.
     *
     * @param request {@link Request}
     * @param responseType the response type
     * @return an object of responseType
     * @throws RestClientException the exception
     */
    <T> T retrieveForObject(Request request, Class<T> responseType) throws RestClientException;

    /**
     * Retrieve a list of objects with responseType by sending a {@link org.apache.http.client.fluent.Request}.
     *
     * @param request {@link Request}
     * @param responseType the response type
     * @return a list of objects
     * @throws RestClientException the exception
     */
    <T> List<T> retrieveForList(Request request, TypeReference<List<T>> responseType) throws RestClientException;

    /**
     * Execute the HTTP Request.
     *
     * @param request {@link Request}
     * @throws RestClientException the exception
     */
    void execute(Request request) throws RestClientException;

    /**
     * Execute the HTTP Request and return the response content as string.
     *
     * @param request {@link Request}
     * @return the response content as string
     * @throws RestClientException the exception
     */
    String executeForString(Request request) throws RestClientException;
}
