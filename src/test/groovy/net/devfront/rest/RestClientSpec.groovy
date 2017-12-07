package net.devfront.rest

import com.fasterxml.jackson.core.type.TypeReference
import net.devfront.rest.exception.RestClientException
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Response
import spock.lang.Specification

/**
 * Unit tests fot RestClient.
 */
class RestClientSpec extends Specification {
    RestClient restClient

    // Stubs
    HttpEntity httpEntity
    StatusLine statusLine
    HttpResponse httpResponse
    Response response
    Request request

    def setup() {
        httpEntity = Stub()
        statusLine = Stub()
        httpResponse = Stub()
        response = Stub()
        request = Stub()

        restClient = new RestClient()
    }


    def "retrieveForObject: should return a single entity"() {
        given:
        def json = '''
            {
                "number": 1,
                "text": "text1"
            }
        '''
        setupHttpMock(json)

        when:
        SimpleResponse result = restClient.retrieveForObject(request, SimpleResponse.class)

        then:
        result != null
        result.number == 1
        result.text == "text1"
    }

    def "retrieveForList: should return a list of entity"() {
        given:
        def json = '''[
            {
                "number": 1,
                "text": "text1"
            },
            {
                "number": 2,
                "text": "text2"
            },
            {
                "number": 3,
                "text": "text3"
            }
        ]'''
        setupHttpMock(json)

        when:
        def list = restClient.retrieveForList(request, new TypeReference<List<SimpleResponse>>() {})

        then:
        list != null
        list.size() == 3
        list[0].number == 1
        list[0].text == "text1"
    }

    def "execute: should throw RestClientException if there is an I/O error"() {
        given:
        request.execute() >> {
            throw new IOException("I/O Error")
        }


        when:
        restClient.execute(request)

        then:
        thrown(RestClientException)
    }

    def "executeForString: should return the response content as string"() {
        given:
        def content = "OK"
        setupHttpMock(content)


        when:
        def result = restClient.executeForString(request)

        then:
        result == content
    }

    def "should throw RestClientHttpException if there is any Http error(4xx or 5xx)"() {
        given:
        statusLine.getStatusCode() >> statusCode
        httpResponse.getStatusLine() >> statusLine
        response.returnResponse() >> httpResponse
        request.execute() >> response

        when:
        restClient.executeForString(request)

        then:
        def ex = thrown(RestClientException)
        ex.getStatusCode() == statusCodeFromException

        where:
        statusCode || statusCodeFromException
        400        || 400
        401        || 401
        402        || 402
        403        || 403
        404        || 404
        500        || 500
        501        || 501
        502        || 502
        503        || 503
        504        || 504
        505        || 505
    }

    def "should throw RestClientException if the Http status code is unknown"() {
        given:
        statusLine.getStatusCode() >> 610
        httpResponse.getStatusLine() >> statusLine
        response.returnResponse() >> httpResponse
        request.execute() >> response

        when:
        restClient.executeForString(request)

        then:
        thrown(RestClientException)
    }

    def setupHttpMock(String json) {
        statusLine.getStatusCode() >> 200
        httpResponse.getStatusLine() >> statusLine

        httpEntity.getContent() >> new ByteArrayInputStream(json.getBytes())
        httpResponse.getEntity() >> httpEntity

        response.returnResponse() >> httpResponse
        request.execute() >> response
    }
}
