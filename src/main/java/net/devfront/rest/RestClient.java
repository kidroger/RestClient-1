package net.devfront.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.devfront.rest.exception.RestClientException;
import net.devfront.rest.exception.RestClientHttpException;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * A lightweight REST client for Java leveraging the HttpClient Fluent API.
 */
public class RestClient implements RestOperations {
    private static final ObjectMapper mapper = new  ObjectMapper();

    /**
     * Retrieve an object of responseType by sending a {@link org.apache.http.client.fluent.Request}.
     *
     * @param request {@link Request}
     * @param responseType the response type
     * @return an object of responseType
     * @throws RestClientException the exception
     */
    @Override
    public <T> T retrieveForObject(Request request, Class<T> responseType) throws RestClientException {
        return bindObject(sendRequestForString(request), responseType);
    }

    /**
     * Retrieve a list of objects with responseType by sending a {@link org.apache.http.client.fluent.Request}.
     *
     * @param request {@link Request}
     * @param responseType the response type
     * @return a list of objects
     * @throws RestClientException the exception
     */
    @Override
    public <T> List<T> retrieveForList(Request request, TypeReference<List<T>> responseType) throws RestClientException {
        return bindJsonArray(sendRequestForString(request), responseType);
    }

    /**
     * Execute the HTTP Request.
     *
     * @param request {@link Request}
     * @throws RestClientException the exception
     */
    @Override
    public void execute(Request request) throws RestClientException {
        sendRequest(request);
    }

    /**
     * Execute the HTTP Request and return the response content as string.
     *
     * @param request {@link Request}
     * @return the response content as string
     * @throws RestClientException the exception
     */
    @Override
    public String executeForString(Request request) throws RestClientException {
        return sendRequestForString(request);
    }

    /**
     * Send request and return the response content as string.
     *
     * @param request the Request
     * @return the response content as string
     * @throws RestClientException the exception
     */
    private String sendRequestForString(Request request) throws RestClientException {
        HttpResponse response = sendRequest(request);
        return getContentFromResponse(response);
    }

    /**
     * Send request and return {@link HttpResponse}.
     * @param request {@link Request}
     * @return the HttpResponse
     * @throws RestClientException any I/O error
     */
    private HttpResponse sendRequest(Request request) throws RestClientException {
        HttpResponse response;

        try {
            response = request.execute().returnResponse();
        } catch (IOException e) {
            throw new RestClientException("I/O error on sending Http request", e);
        }

        validateResponse(response);

        return response;
    }

    /**
     * Returns the content as string from the HttpResponse.
     * @param response the HttpResponse
     * @return the content as string
     * @throws RestClientException I/O error
     */
    private String getContentFromResponse(HttpResponse response) throws RestClientException {
        try {
            return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RestClientException("I/O error", e);
        }
    }

    /**
     * Validate the HttpResponse.
     *
     * @param response the HttpResponse
     * @throws RestClientException the exception
     */
    private void validateResponse(HttpResponse response) throws RestClientException {
        int statusCode = getHttpStatusCode(response);

        try {
            HttpStatusSeries httpStatusSeries = HttpStatusSeries.valueOf(statusCode);

            if (hasError(httpStatusSeries)) {
                throw new RestClientHttpException("Http error", statusCode, getHttpStatusText(response));
            }
        } catch (IllegalArgumentException e) {
            throw new RestClientException(String.format("Unknown status code [%s]", statusCode), e);
        }
    }

    /**
     * Check if the given Http status code is error, either 4xx or 5xx.
     *
     * @param httpStatusSeries the {@link HttpStatusSeries}
     * @return boolean
     */
    private boolean hasError(HttpStatusSeries httpStatusSeries) {
        return httpStatusSeries == HttpStatusSeries.CLIENT_ERROR || httpStatusSeries == HttpStatusSeries.SERVER_ERROR;
    }

    /**
     * Returns the Http status code.
     *
     * @param response the HttpResponse
     * @return the Http status code
     */
    private int getHttpStatusCode(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    /**
     * Returns the Http status text.
     *
     * @param response the HttpResponse
     * @return the Http status text
     */
    private String getHttpStatusText(HttpResponse response) {
        return response.getStatusLine().getReasonPhrase();
    }

    /**
     * Convert JSON to entity.
     *
     * @param source the JSON
     * @param entityClass the entity class
     * @return the entity
     * @throws RestClientException I/O error
     */
    private  <T> T bindObject(String source, Class<T> entityClass) throws RestClientException {
        try {
            return mapper.readValue(source, entityClass);
        } catch (IOException e) {
            throw new RestClientException("Error on converting JSON to entity", e);
        }
    }

    /**
     * Convert JSON to list of entity.
     *
     * @param source the JSON
     * @param type the entity type
     * @return list of entity
     * @throws RestClientException I/O error
     */
    private <T> List<T> bindJsonArray(String source, TypeReference<List<T>> type) throws RestClientException {
        try {
            return mapper.readValue(source, type);
        } catch (IOException e) {
            throw new RestClientException("Error on converting JSON to a list of entity", e);
        }
    }
}
